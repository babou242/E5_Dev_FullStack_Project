<template>
  <div class="book-detail">
    <!-- Chargement -->
    <div v-if="booksStore.loading" class="loading">
      Chargement du livre...
    </div>

    <!-- Erreur -->
    <div v-else-if="booksStore.error" class="error">
      {{ booksStore.error }}
      <RouterLink to="/books" class="btn">Retour au catalogue</RouterLink>
    </div>

    <!-- Contenu -->
    <template v-else-if="book">
      <header class="header">
        <RouterLink to="/books" class="back-link">← Retour au catalogue</RouterLink>
      </header>

      <article class="book-content">
        <div class="book-main">
          <div class="book-cover">
            <img
              v-if="book.coverUrl"
              :src="book.coverUrl"
              :alt="book.title"
            />
            <div v-else class="placeholder-cover">
              📚
            </div>
          </div>

          <div class="book-info">
            <span class="badge">{{ BookCategoryLabels[book.category] }}</span>
            <h1>{{ book.title }}</h1>
            <p class="author">par <strong>{{ book.author }}</strong></p>

            <div class="meta">
              <p><strong>ISBN:</strong> {{ book.isbn }}</p>
              <p v-if="book.publicationYear">
                <strong>Année:</strong> {{ book.publicationYear }}
              </p>
            </div>

            <p class="price">{{ book.price.toFixed(2) }} €</p>

            <!-- Actions admin -->
            <div v-if="authStore.isAdmin" class="admin-actions">
              <RouterLink
                :to="`/admin/books/${book.id}/edit`"
                class="btn secondary"
              >
                Éditer
              </RouterLink>
              <button class="btn danger" @click="handleDelete">
                Supprimer
              </button>
            </div>
          </div>
        </div>

        <section v-if="book.description" class="description">
          <h2>Description</h2>
          <p>{{ book.description }}</p>
        </section>
      </article>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBooksStore } from '@/stores/book'
import { useAuthStore } from '@/stores/auth'
import { BookCategoryLabels } from '@/types'

const route = useRoute()
const router = useRouter()
const booksStore = useBooksStore()
const authStore = useAuthStore()

const book = computed(() => booksStore.currentBook)

async function handleDelete() {
  if (!book.value?.id) return

  if (confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
    try {
      await booksStore.deleteBook(book.value.id)
      router.push('/books')
    } catch (error) {
      console.error('Erreur lors de la suppression:', error)
    }
  }
}

onMounted(() => {
  const id = Number(route.params.id)
  if (id) {
    booksStore.fetchBook(id)
  }
})
</script>

<style scoped>
.book-detail {
  width: min(900px, 92%);
  margin: 0 auto;
}

.loading,
.error {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.error {
  color: #c62828;
}

.header {
  margin-bottom: 24px;
}

.back-link {
  color: #1f6feb;
  text-decoration: none;
  font-weight: 600;
}

.back-link:hover {
  text-decoration: underline;
}

.book-content {
  background: white;
  border: 1px solid #e6e6e6;
  border-radius: 16px;
  padding: 30px;
}

.book-main {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}

.book-cover {
  flex-shrink: 0;
  width: 200px;
  height: 280px;
  border-radius: 10px;
  overflow: hidden;
  background: #f5f5f5;
}

.book-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder-cover {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 4rem;
  background: linear-gradient(135deg, #f0f4ff 0%, #e0e8ff 100%);
}

.book-info {
  flex: 1;
  min-width: 250px;
}

.badge {
  display: inline-block;
  font-size: 12px;
  font-weight: 800;
  background: #f3f5ff;
  border: 1px solid #dbe2ff;
  padding: 4px 12px;
  border-radius: 999px;
  margin-bottom: 12px;
}

.book-info h1 {
  margin: 0 0 8px 0;
  font-size: 1.8rem;
}

.author {
  margin: 0 0 20px 0;
  font-size: 1.1rem;
  color: #555;
}

.meta {
  margin-bottom: 20px;
}

.meta p {
  margin: 4px 0;
  color: #666;
}

.price {
  font-size: 1.5rem;
  font-weight: 900;
  color: #1f6feb;
  margin: 0 0 20px 0;
}

.admin-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.btn {
  display: inline-block;
  text-decoration: none;
  padding: 10px 18px;
  border-radius: 10px;
  font-weight: 700;
  border: none;
  cursor: pointer;
  font-size: 0.95rem;
}

.btn.secondary {
  background: #eaeaea;
  color: #111;
}

.btn.danger {
  background: #c62828;
  color: white;
}

.description {
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid #e6e6e6;
}

.description h2 {
  margin: 0 0 12px 0;
  font-size: 1.3rem;
}

.description p {
  margin: 0;
  line-height: 1.7;
  color: #444;
}
</style>
