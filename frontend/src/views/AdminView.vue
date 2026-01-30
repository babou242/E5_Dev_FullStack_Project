<template>
  <div class="admin-view">
    <header class="page-header">
      <div>
        <h1>Administration</h1>
        <p class="subtitle">Gérez les livres de votre catalogue</p>
      </div>
      <RouterLink to="/admin/books/new" class="btn primary">
        + Ajouter un livre
      </RouterLink>
    </header>

    <!-- Statistiques -->
    <section class="stats-grid">
      <div class="stat-card">
        <span class="stat-value">{{ booksStore.books.length }}</span>
        <span class="stat-label">Livres au total</span>
      </div>
      <div
        v-for="(count, category) in booksStore.categoryStats"
        :key="category"
        class="stat-card"
      >
        <span class="stat-value">{{ count }}</span>
        <span class="stat-label">{{ BookCategoryLabels[category as BookCategory] }}</span>
      </div>
    </section>

    <!-- Chargement -->
    <div v-if="booksStore.loading" class="loading">
      Chargement des livres...
    </div>

    <!-- Erreur -->
    <div v-else-if="booksStore.error" class="error">
      {{ booksStore.error }}
      <button @click="booksStore.fetchBooks()">Réessayer</button>
    </div>

    <!-- Liste des livres -->
    <section v-else class="books-section">
      <h2>Tous les livres</h2>
      <div class="books-grid">
        <BookCard
          v-for="book in booksStore.books"
          :key="book.id"
          :book="book"
          :showAdminActions="true"
          @delete="handleDelete"
        />
      </div>

      <p v-if="booksStore.books.length === 0" class="no-books">
        Aucun livre dans le catalogue. Commencez par en ajouter un !
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

async function handleDelete(id: number) {
  if (confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
    try {
      await booksStore.deleteBook(id)
    } catch (error) {
      console.error('Erreur lors de la suppression:', error)
    }
  }
}

onMounted(() => {
  booksStore.fetchBooks()
})
</script>

<style scoped>
.admin-view {
  width: min(1100px, 92%);
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-header h1 {
  margin: 0;
  font-size: 1.8rem;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.subtitle {
  margin: 4px 0 0 0;
  color: rgba(255, 255, 255, 0.9);
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.btn {
  display: inline-block;
  text-decoration: none;
  padding: 12px 20px;
  border-radius: 10px;
  font-weight: 700;
  font-size: 0.95rem;
}

.btn.primary {
  background: #1f6feb;
  color: white;
}

.btn.primary:hover {
  background: #1a5cc7;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 16px;
  margin-bottom: 40px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid #e6e6e6;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.stat-value {
  display: block;
  font-size: 2rem;
  font-weight: 800;
  color: #1f6feb;
}

.stat-label {
  display: block;
  font-size: 0.85rem;
  color: #666;
  margin-top: 4px;
}

.loading,
.error {
  text-align: center;
  padding: 40px;
  color: #666;
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

.books-section h2 {
  margin: 0 0 20px 0;
  font-size: 1.3rem;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.no-books {
  text-align: center;
  color: #888;
  padding: 40px;
  background: #f9f9f9;
  border-radius: 12px;
}
</style>
