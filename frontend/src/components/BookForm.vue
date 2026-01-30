<template>
  <form class="book-form" @submit.prevent="handleSubmit">
    <!-- Titre -->
    <div class="field">
      <label for="title">Titre *</label>
      <input
          id="title"
          type="text"
          v-model="formData.title"
          @blur="handleBlur('title')"
          @input="handleInput('title')"
          placeholder="Ex: Les Misérables"
      />
      <p v-if="touched.title && errors.title" class="error">{{ errors.title }}</p>
    </div>

    <!-- Auteur -->
    <div class="field">
      <label for="author">Auteur *</label>
      <input
          id="author"
          type="text"
          v-model="formData.author"
          @blur="handleBlur('author')"
          @input="handleInput('author')"
          placeholder="Ex: Victor Hugo"
      />
      <p v-if="touched.author && errors.author" class="error">{{ errors.author }}</p>
    </div>

    <!-- ISBN -->
    <div class="field">
      <label for="isbn">ISBN *</label>
      <input
          id="isbn"
          type="text"
          v-model="formData.isbn"
          @blur="handleBlur('isbn')"
          @input="handleInput('isbn')"
          placeholder="978-1234567890"
      />
      <p v-if="touched.isbn && errors.isbn" class="error">{{ errors.isbn }}</p>
    </div>

    <!-- Prix -->
    <div class="field">
      <label for="price">Prix (€) *</label>
      <input
          id="price"
          type="number"
          step="0.01"
          v-model.number="formData.price"
          @blur="handleBlur('price')"
          @input="handleInput('price')"
          placeholder="Ex: 12.99"
      />
      <p v-if="touched.price && errors.price" class="error">{{ errors.price }}</p>
    </div>

    <!-- Catégorie -->
    <div class="field">
      <label for="category">Catégorie *</label>
      <select
          id="category"
          v-model="formData.category"
          @blur="handleBlur('category')"
          @change="handleInput('category')"
      >
        <option value="" disabled>Sélectionnez une catégorie</option>
        <option
            v-for="cat in categories"
            :key="cat"
            :value="cat"
        >
          {{ BookCategoryLabels[cat] }}
        </option>
      </select>
      <p v-if="touched.category && errors.category" class="error">{{ errors.category }}</p>
    </div>

    <!-- Année de publication -->
    <div class="field">
      <label for="publicationYear">Année de publication *</label>
      <input
          id="publicationYear"
          type="number"
          v-model.number="formData.publicationYear"
          @blur="handleBlur('publicationYear')"
          @input="handleInput('publicationYear')"
          :min="1800"
          :max="currentYear"
          placeholder="Ex: 1862"
      />
      <p
          v-if="touched.publicationYear && errors.publicationYear"
          class="error"
      >
        {{ errors.publicationYear }}
      </p>
    </div>

    <!-- Description -->
    <div class="field">
      <label for="description">Description</label>
      <textarea
          id="description"
          v-model="formData.description"
          @blur="handleBlur('description')"
          @input="handleInput('description')"
          rows="5"
          maxlength="1000"
          placeholder="Description (optionnel, 1000 caractères max)"
      ></textarea>

      <div class="meta">
        <small class="counter">
          {{ (formData.description || '').length }} / 1000
        </small>
      </div>

      <p
          v-if="touched.description && errors.description"
          class="error"
      >
        {{ errors.description }}
      </p>
    </div>

    <!-- Actions -->
    <div class="actions">
      <button type="button" class="btn secondary" @click="emit('cancel')">
        Annuler
      </button>

      <button type="submit" class="btn primary" :disabled="!isValid">
        {{ props.book ? 'Mettre à jour' : 'Créer' }}
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useValidation, validationRules } from '@/composables/useValidation'
import type { Book, BookCategory } from '@/types'
import { BookCategoryLabels, BookCategory as BookCategoryEnum } from '@/types'

const props = defineProps<{
  book?: Book // Si fourni, mode édition
}>()

const emit = defineEmits<{
  submit: [book: Omit<Book, 'id'>]
  cancel: []
}>()

const currentYear = new Date().getFullYear()

const { formData, errors, touched, handleBlur, handleInput, validateAll, isValid } =
    useValidation(
        {
          title: props.book?.title || '',
          author: props.book?.author || '',
          isbn: props.book?.isbn || '',
          price: props.book?.price || 0,
          description: props.book?.description || '',
          category: (props.book?.category || '') as BookCategory,
          publicationYear: props.book?.publicationYear || currentYear
        },
        {
          title: [
            validationRules.required(),
            validationRules.minLength(2),
            validationRules.maxLength(200)
          ],
          author: [
            validationRules.required(),
            validationRules.minLength(2),
            validationRules.maxLength(100)
          ],
          isbn: [
            validationRules.required(),
            validationRules.isbn()
          ],
          price: [
            validationRules.required(),
            validationRules.positiveNumber()
          ],
          description: [
            validationRules.maxLength(1000)
          ],
          category: [
            validationRules.required('Sélectionnez une catégorie')
          ],
          publicationYear: [
            validationRules.required(),
            validationRules.year(1800, currentYear)
          ]
        }
    )

const categories = computed(() => {
  // Enum TS -> liste des valeurs
  return Object.values(BookCategoryEnum) as BookCategory[]
})

function handleSubmit() {
  if (validateAll()) {
    // On n'envoie pas l'id (exigence du TP)
    const payload: Omit<Book, 'id'> = {
      title: formData.title,
      author: formData.author,
      isbn: formData.isbn,
      price: formData.price,
      description: formData.description || undefined,
      category: formData.category,
      publicationYear: formData.publicationYear,
      coverUrl: (formData as any).coverUrl // optionnel si tu l’ajoutes plus tard
    }
    emit('submit', payload)
  }
}
</script>

<style scoped>
.book-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

input,
select,
textarea {
  padding: 10px;
  border: 1px solid #cfcfcf;
  border-radius: 8px;
  font-size: 14px;
}

textarea {
  resize: vertical;
}

.error {
  color: #c62828;
  font-size: 13px;
  margin: 0;
}

.meta {
  display: flex;
  justify-content: flex-end;
}

.counter {
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 6px;
}

.btn {
  padding: 10px 14px;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  font-weight: 600;
}

.btn.primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn.primary {
  background: #1f6feb;
  color: white;
}

.btn.secondary {
  background: #e6e6e6;
}
</style>
