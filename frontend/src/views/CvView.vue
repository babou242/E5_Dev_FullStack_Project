<template>
  <div class="cv-container">
    <h1>Mon CV</h1>
    <div class="pdf-wrapper">
      <iframe 
        :src="cvUrl" 
        class="pdf-viewer"
        title="CV"
      ></iframe>
    </div>
    <a :href="cvUrl" download class="download-btn">
      📥 Télécharger le CV
    </a>

    <!-- Section Avis -->
    <section class="reviews-section">
      <div class="reviews-header">
        <h2>Avis sur mon CV</h2>
        <div v-if="stats" class="stats-summary">
          <span class="average-rating">
            ⭐ {{ stats.averageRating }}/5
          </span>
          <span class="total-reviews">
            ({{ stats.totalReviews }} avis)
          </span>
        </div>
      </div>

      <!-- Formulaire d'ajout d'avis (si connecté) -->
      <div v-if="auth.isAuthenticated" class="review-form-container">
        <h3>Laisser un avis</h3>
        <p class="connected-as">Connecté en tant que <strong>{{ auth.username }}</strong></p>
        <form @submit.prevent="submitReview" class="review-form">
          <div class="form-row">
            <div class="field">
              <label for="authorName">Votre nom *</label>
              <input
                id="authorName"
                type="text"
                v-model="newReview.authorName"
                placeholder="Jean Dupont"
                required
                minlength="2"
                maxlength="100"
              />
            </div>
            <div class="field">
              <label for="email">Votre email (optionnel)</label>
              <input
                id="email"
                type="email"
                v-model="newReview.email"
                placeholder="jean@example.com"
              />
            </div>
          </div>

          <div class="field">
            <label>Note *</label>
            <div class="rating-input">
              <button
                v-for="star in 5"
                :key="star"
                type="button"
                class="star-btn"
                :class="{ active: star <= newReview.rating }"
                @click="newReview.rating = star"
              >
                ★
              </button>
            </div>
          </div>

          <div class="field">
            <label for="comment">Votre commentaire *</label>
            <textarea
              id="comment"
              v-model="newReview.comment"
              placeholder="Donnez votre avis sur ce CV..."
              required
              minlength="10"
              maxlength="1000"
              rows="4"
            ></textarea>
          </div>

          <p v-if="formError" class="error-message">{{ formError }}</p>
          <p v-if="formSuccess" class="success-message">{{ formSuccess }}</p>

          <button type="submit" class="submit-btn" :disabled="submitting">
            {{ submitting ? 'Envoi...' : 'Envoyer mon avis' }}
          </button>
        </form>
      </div>

      <!-- Message si non connecté -->
      <div v-else class="login-prompt">
        <h3>🔒 Connectez-vous pour laisser un avis</h3>
        <p>Vous devez être connecté pour pouvoir donner votre avis sur ce CV.</p>
        <RouterLink to="/login" class="login-btn">Se connecter</RouterLink>
      </div>

      <!-- Liste des avis -->
      <div class="reviews-list">
        <h3>Tous les avis ({{ reviews.length }})</h3>
        
        <div v-if="loading" class="loading">Chargement des avis...</div>
        
        <div v-else-if="reviews.length === 0" class="no-reviews">
          Aucun avis pour le moment. Soyez le premier à donner votre avis !
        </div>

        <article v-else v-for="review in reviews" :key="review.id" class="review-card">
          <div class="review-header">
            <div class="review-author">
              <strong>{{ review.authorName }}</strong>
              <span class="review-date">{{ formatDate(review.createdAt) }}</span>
            </div>
            <div class="review-actions">
              <div class="review-rating">
                <span v-for="star in 5" :key="star" class="star" :class="{ filled: star <= review.rating }">
                  ★
                </span>
              </div>
              <button 
                v-if="auth.isAdmin" 
                class="delete-btn" 
                @click="deleteReview(review.id!)"
                title="Supprimer cet avis"
              >
                🗑️
              </button>
            </div>
          </div>
          <p class="review-comment">{{ review.comment }}</p>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import api from '@/services/api'
import { useAuthStore } from '@/stores/auth'
import type { CvReview, CvReviewStats } from '@/types'

// Store d'authentification
const auth = useAuthStore()

// Place ton fichier CV.pdf dans le dossier public/
const cvUrl = '/CV.pdf'

const reviews = ref<CvReview[]>([])
const stats = ref<CvReviewStats | null>(null)
const loading = ref(true)
const submitting = ref(false)
const formError = ref('')
const formSuccess = ref('')

const newReview = reactive<Omit<CvReview, 'id' | 'createdAt'>>({
  authorName: '',
  email: '',
  comment: '',
  rating: 5
})

async function fetchReviews() {
  try {
    loading.value = true
    const [reviewsRes, statsRes] = await Promise.all([
      api.get<CvReview[]>('/cv-reviews'),
      api.get<CvReviewStats>('/cv-reviews/stats')
    ])
    reviews.value = reviewsRes.data
    stats.value = statsRes.data
  } catch (error) {
    console.error('Erreur lors du chargement des avis:', error)
  } finally {
    loading.value = false
  }
}

async function submitReview() {
  formError.value = ''
  formSuccess.value = ''

  if (newReview.rating < 1 || newReview.rating > 5) {
    formError.value = 'Veuillez sélectionner une note entre 1 et 5'
    return
  }

  try {
    submitting.value = true
    await api.post('/cv-reviews', newReview)
    
    // Reset form
    newReview.authorName = ''
    newReview.email = ''
    newReview.comment = ''
    newReview.rating = 5
    
    formSuccess.value = 'Merci pour votre avis !'
    
    // Refresh reviews
    await fetchReviews()
    
    setTimeout(() => {
      formSuccess.value = ''
    }, 3000)
  } catch (error: any) {
    console.error('Erreur lors de l\'envoi:', error)
    formError.value = error.response?.data?.message || 'Erreur lors de l\'envoi de votre avis'
  } finally {
    submitting.value = false
  }
}

function formatDate(dateString?: string): string {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

async function deleteReview(id: number) {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cet avis ?')) {
    return
  }

  try {
    await api.delete(`/cv-reviews/${id}`)
    await fetchReviews()
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    alert('Erreur lors de la suppression de l\'avis')
  }
}
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

onMounted(() => {
  fetchReviews()
})
</script>

<style scoped>
.cv-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

h1 {
  margin-bottom: 20px;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.pdf-wrapper {
  width: 100%;
  height: 80vh;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  background: white;
}

.pdf-viewer {
  width: 100%;
  height: 100%;
  border: none;
}

.download-btn {
  display: inline-block;
  margin-top: 20px;
  padding: 12px 24px;
  background-color: #1f6feb;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 600;
  transition: background-color 0.2s;
}

.download-btn:hover {
  background-color: #1558c7;
}

/* Section Avis */
.reviews-section {
  margin-top: 50px;
  text-align: left;
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 16px;
}

.reviews-header h2 {
  margin: 0;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.stats-summary {
  background: rgba(255, 255, 255, 0.95);
  padding: 10px 20px;
  border-radius: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.average-rating {
  font-size: 1.2rem;
  font-weight: 700;
  color: #f59e0b;
}

.total-reviews {
  color: #666;
  margin-left: 8px;
}

/* Formulaire */
.review-form-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.review-form-container h3 {
  margin: 0 0 20px 0;
  font-size: 1.3rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 600px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}

.field {
  margin-bottom: 20px;
}

.field label {
  display: block;
  margin-bottom: 6px;
  font-weight: 600;
  color: #333;
}

.field input,
.field textarea {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  box-sizing: border-box;
  font-family: inherit;
}

.field input:focus,
.field textarea:focus {
  outline: none;
  border-color: #1f6feb;
}

.rating-input {
  display: flex;
  gap: 8px;
}

.star-btn {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: #ddd;
  transition: color 0.2s, transform 0.1s;
}

.star-btn:hover,
.star-btn.active {
  color: #f59e0b;
}

.star-btn:hover {
  transform: scale(1.1);
}

.error-message {
  color: #c62828;
  background: #ffebee;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.success-message {
  color: #2e7d32;
  background: #e8f5e9;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  background: #1f6feb;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
}

.submit-btn:hover:not(:disabled) {
  background: #1558c7;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Liste des avis */
.reviews-list {
  margin-top: 30px;
}

.reviews-list h3 {
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  margin-bottom: 20px;
}

.loading,
.no-reviews {
  background: rgba(255, 255, 255, 0.9);
  padding: 30px;
  border-radius: 12px;
  text-align: center;
  color: #666;
}

.review-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  flex-wrap: wrap;
  gap: 10px;
}

.review-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.review-author strong {
  display: block;
  font-size: 1.1rem;
}

.review-date {
  font-size: 0.85rem;
  color: #888;
}

.review-rating {
  display: flex;
  gap: 2px;
}

.star {
  color: #ddd;
  font-size: 1.2rem;
}

.star.filled {
  color: #f59e0b;
}

.review-comment {
  margin: 0;
  color: #444;
  line-height: 1.6;
}

/* Message connexion requise */
.login-prompt {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  text-align: center;
}

.login-prompt h3 {
  margin: 0 0 12px 0;
  font-size: 1.4rem;
  color: #333;
}

.login-prompt p {
  margin: 0 0 24px 0;
  color: #666;
}

.login-btn {
  display: inline-block;
  padding: 14px 32px;
  background: #1f6feb;
  color: white;
  text-decoration: none;
  border-radius: 10px;
  font-weight: 700;
  transition: background 0.2s;
}

.login-btn:hover {
  background: #1558c7;
  text-decoration: none;
}

.connected-as {
  margin: 0 0 20px 0;
  color: #666;
  font-size: 0.9rem;
}

.delete-btn {
  background: #fee2e2;
  border: 1px solid #fca5a5;
  border-radius: 8px;
  padding: 6px 10px;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s;
}

.delete-btn:hover {
  background: #fecaca;
}
</style>
