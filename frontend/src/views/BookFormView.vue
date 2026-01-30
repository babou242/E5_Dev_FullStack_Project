<template>
  <div class="book-form-view">
    <header class="page-header">
      <RouterLink to="/admin" class="back-link">← Retour à l'admin</RouterLink>
      <h1>{{ isEditMode ? 'Modifier le livre' : 'Ajouter un livre' }}</h1>
    </header>

    <!-- Chargement en mode édition -->
    <div v-if="isEditMode && booksStore.loading" class="loading">
      Chargement du livre...
    </div>

    <!-- Erreur -->
    <div v-else-if="isEditMode && booksStore.error" class="error">
      {{ booksStore.error }}
      <RouterLink to="/admin" class="btn">Retour</RouterLink>
    </div>

    <!-- Formulaire -->
    <div v-else class="form-container">
      <BookForm
        :book="isEditMode ? booksStore.currentBook ?? undefined : undefined"
        @submit="handleSubmit"
        @cancel="router.push('/admin')"
      />

      <p v-if="submitError" class="submit-error">
        {{ submitError }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBooksStore } from '@/stores/book'
import BookForm from '@/components/BookForm.vue'
import type { Book } from '@/types'

const route = useRoute()
const router = useRouter()
const booksStore = useBooksStore()

const submitError = ref('')

// Déterminer si on est en mode édition
const isEditMode = computed(() => route.name === 'book-edit')
const bookId = computed(() => Number(route.params.id))

async function handleSubmit(bookData: Omit<Book, 'id'>) {
  submitError.value = ''

  try {
    if (isEditMode.value && bookId.value) {
      await booksStore.updateBook(bookId.value, bookData)
    } else {
      await booksStore.createBook(bookData)
    }

    router.push('/admin')
  } catch (error: any) {
    console.error('Erreur lors de la sauvegarde:', error)
    submitError.value = error.response?.data?.message || 'Erreur lors de la sauvegarde du livre'
  }
}

onMounted(async () => {
  if (isEditMode.value && bookId.value) {
    await booksStore.fetchBook(bookId.value)
  }
})
</script>

<style scoped>
.book-form-view {
  width: min(700px, 92%);
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
}

.back-link {
  display: inline-block;
  color: white;
  text-decoration: none;
  font-weight: 600;
  margin-bottom: 12px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.back-link:hover {
  text-decoration: underline;
}

.page-header h1 {
  margin: 0;
  font-size: 1.8rem;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
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

.form-container {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid #e6e6e6;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
}

.submit-error {
  color: #c62828;
  background: #ffebee;
  padding: 12px 16px;
  border-radius: 8px;
  margin-top: 16px;
  text-align: center;
}

.btn {
  display: inline-block;
  text-decoration: none;
  padding: 10px 18px;
  border-radius: 10px;
  font-weight: 700;
  background: #1f6feb;
  color: white;
  margin-top: 16px;
}
</style>
