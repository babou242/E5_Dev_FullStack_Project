<template>
  <article class="card">
    <div class="top">
      <h3 class="title">{{ book.title }}</h3>
      <span class="badge">{{ book.category }}</span>
    </div>

    <p class="author">{{ book.author }} • {{ book.publicationYear }}</p>
    <p class="isbn">ISBN: {{ book.isbn }}</p>

    <p class="price">{{ book.price.toFixed(2) }} €</p>

    <div class="actions">
      <RouterLink class="btn" :to="`/books/${book.id}`">Détail</RouterLink>

      <template v-if="showAdminActions">
        <RouterLink class="btn secondary" :to="`/admin/books/${book.id}/edit`">
          Éditer
        </RouterLink>
        <button class="btn danger" @click="$emit('delete', book.id!)">Supprimer</button>
      </template>
    </div>
  </article>
</template>

<script setup lang="ts">
import type { Book } from '@/types'

defineProps<{
  book: Book
  showAdminActions?: boolean
}>()

defineEmits<{
  delete: [id: number]
}>()
</script>

<style scoped>
.card {
  background: white;
  border: 1px solid #e6e6e6;
  border-radius: 14px;
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
.top {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: flex-start;
}
.title {
  margin: 0;
}
.badge {
  font-size: 12px;
  font-weight: 800;
  background: #f3f5ff;
  border: 1px solid #dbe2ff;
  padding: 4px 8px;
  border-radius: 999px;
}
.author, .isbn {
  margin: 0;
  color: #444;
}
.price {
  margin: 0;
  font-weight: 900;
}
.actions {
  display: flex;
  gap: 8px;
  margin-top: 6px;
  flex-wrap: wrap;
}
.btn {
  text-decoration: none;
  background: #1f6feb;
  color: white;
  border: none;
  padding: 8px 10px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 800;
}
.secondary {
  background: #eaeaea;
  color: #111;
}
.danger {
  background: #c62828;
}
</style>
