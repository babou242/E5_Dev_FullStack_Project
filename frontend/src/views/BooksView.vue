<template>
  <div class="books-view">
    <header class="page-header">
      <h1>Catalogue des livres</h1>
    </header>

    <!-- Filtres -->
    <section class="filters">
      <div class="search-box">
        <input
          type="text"
          v-model="booksStore.searchQuery"
          placeholder="Rechercher par titre ou auteur..."
        />
      </div>

      <div class="category-filter">
        <select v-model="booksStore.selectedCategory">
          <option :value="null">Toutes les catégories</option>
          <option
            v-for="cat in categories"
            :key="cat"
            :value="cat"
          >
            {{ BookCategoryLabels[cat] }}
          </option>
        </select>
      </div>
    </section>

    <!-- Statistiques -->
    <section class="stats">
      <span class="stat">
        {{ booksStore.filteredBooks.length }} livre(s) trouvé(s)
      </span>
      <div class="category-stats">
        <span
          v-for="(count, category) in booksStore.categoryStats"
          :key="category"
          class="stat-badge"
        >
          {{ BookCategoryLabels[category as BookCategory] }}: {{ count }}
        </span>
      </div>
    </section>

    <!-- État de chargement -->
    <div v-if="booksStore.loading" class="loading">
      Chargement des livres...
    </div>

    <!-- Erreur -->
    <div v-else-if="booksStore.error" class="error">
      {{ booksStore.error }}
      <button @click="booksStore.fetchBooks()">Réessayer</button>
    </div>

    <!-- Liste des livres -->
    <section v-else class="books-grid">
      <BookCard
        v-for="book in booksStore.filteredBooks"
        :key="book.id"
        :book="book"
      />

      <p v-if="booksStore.filteredBooks.length === 0" class="no-results">
        Aucun livre ne correspond à votre recherche.
      </p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useBooksStore } from '@/stores/book'
import { BookCategory, BookCategoryLabels } from '@/types'
import BookCard from '@/components/BookCard.vue'

const booksStore = useBooksStore()

// Liste des catégories pour le select
const categories = Object.values(BookCategory)

onMounted(() => {
  booksStore.fetchBooks()
})
</script>

<style scoped>
.books-view {
  width: min(1000px, 92%);
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  font-size: 1.8rem;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 200px;
}

.search-box input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.search-box input:focus {
  outline: none;
  border-color: #1f6feb;
}

.category-filter select {
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  background: rgba(255, 255, 255, 0.95);
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.stat {
  font-weight: 600;
  color: white;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.category-stats {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.stat-badge {
  font-size: 12px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #dbe2ff;
  padding: 4px 10px;
  border-radius: 999px;
  color: #444;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.loading,
.error {
  text-align: center;
  padding: 40px;
  color: white;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
}

.error {
  color: #c62828;
}

.error button {
  margin-top: 12px;
  padding: 8px 16px;
  background: #1f6feb;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.no-results {
  grid-column: 1 / -1;
  text-align: center;
  color: #888;
  padding: 40px;
}
</style>
