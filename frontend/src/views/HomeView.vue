<template>
  <div class="home">
    <section class="hero">
      <h1>Bienvenue aux Belles Lettres</h1>
      <p class="subtitle">
        Découvrez notre collection de livres classiques et contemporains
      </p>
      <RouterLink class="btn primary" to="/books">
        Explorer le catalogue
      </RouterLink>
    </section>

    <section class="features">
      <div class="feature">
        <span class="icon">📚</span>
        <h3>Large Collection</h3>
        <p>Des milliers de livres dans toutes les catégories</p>
      </div>
      <div class="feature">
        <span class="icon">🔍</span>
        <h3>Recherche Facile</h3>
        <p>Trouvez rapidement le livre que vous cherchez</p>
      </div>
      <div class="feature">
        <span class="icon">💰</span>
        <h3>Meilleurs Prix</h3>
        <p>Des prix compétitifs sur tous nos ouvrages</p>
      </div>
    </section>

    <section v-if="!loading && recentBooks.length" class="recent">
      <h2>Derniers ajouts</h2>
      <div class="books-grid">
        <BookCard
          v-for="book in recentBooks"
          :key="book.id"
          :book="book"
        />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useBooksStore } from '@/stores/book'
import BookCard from '@/components/BookCard.vue'

const booksStore = useBooksStore()

const loading = computed(() => booksStore.loading)

// Afficher les 3 derniers livres
const recentBooks = computed(() => booksStore.books.slice(0, 3))

onMounted(() => {
  if (booksStore.books.length === 0) {
    booksStore.fetchBooks()
  }
})
</script>

<style scoped>
.home {
  width: min(1000px, 92%);
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  margin-bottom: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.hero h1 {
  font-size: 2.5rem;
  margin: 0 0 16px 0;
  color: #111;
}

.subtitle {
  font-size: 1.2rem;
  color: #555;
  margin: 0 0 24px 0;
}

.btn {
  display: inline-block;
  text-decoration: none;
  padding: 14px 28px;
  border-radius: 10px;
  font-weight: 700;
  font-size: 1rem;
}

.btn.primary {
  background: #1f6feb;
  color: white;
}

.btn.primary:hover {
  background: #1a5cc7;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin-bottom: 50px;
}

.feature {
  text-align: center;
  padding: 30px 20px;
  border: 1px solid #e6e6e6;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.feature .icon {
  font-size: 2.5rem;
  display: block;
  margin-bottom: 12px;
}

.feature h3 {
  margin: 0 0 8px 0;
  font-size: 1.2rem;
}

.feature p {
  margin: 0;
  color: #666;
}

.recent h2 {
  margin: 0 0 20px 0;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}
</style>
