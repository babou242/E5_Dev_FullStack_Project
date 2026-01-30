<template>
  <div class="login-view">
    <div class="login-card">
      <h1>Connexion</h1>
      <p class="subtitle">Accédez à votre espace administrateur</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="field">
          <label for="username">Nom d'utilisateur</label>
          <input
            id="username"
            type="text"
            v-model="credentials.username"
            placeholder="Entrez votre nom d'utilisateur"
            required
            :disabled="loading"
          />
        </div>

        <div class="field">
          <label for="password">Mot de passe</label>
          <input
            id="password"
            type="password"
            v-model="credentials.password"
            placeholder="Entrez votre mot de passe"
            required
            :disabled="loading"
          />
        </div>

        <p v-if="error" class="error-message">{{ error }}</p>

        <button type="submit" class="btn primary" :disabled="loading">
          {{ loading ? 'Connexion...' : 'Se connecter' }}
        </button>
      </form>

      <p class="demo-info">
        <strong>Compte démo:</strong><br />
        Utilisateur: admin / Mot de passe: admin
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import type { LoginCredentials } from '@/types'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const credentials = reactive<LoginCredentials>({
  username: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''

  try {
    await authStore.login(credentials)

    // Redirection vers la page demandée ou l'accueil
    const redirect = route.query.redirect as string
    router.push(redirect || '/')
  } catch (e: any) {
    if (e.response?.status === 401) {
      error.value = 'Identifiants incorrects'
    } else {
      error.value = 'Erreur de connexion. Veuillez réessayer.'
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-view {
  min-height: calc(100vh - 100px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: white;
  border: 1px solid #e6e6e6;
  border-radius: 16px;
  padding: 40px 30px;
  text-align: center;
}

.login-card h1 {
  margin: 0 0 8px 0;
  font-size: 1.8rem;
}

.subtitle {
  margin: 0 0 30px 0;
  color: #666;
}

.login-form {
  text-align: left;
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

.field input {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
  box-sizing: border-box;
}

.field input:focus {
  outline: none;
  border-color: #1f6feb;
}

.field input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.error-message {
  color: #c62828;
  background: #ffebee;
  padding: 10px 14px;
  border-radius: 8px;
  margin-bottom: 16px;
  font-size: 0.9rem;
}

.btn {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  margin-bottom: 20px;
}

.btn.primary {
  background: #1f6feb;
  color: white;
}

.btn.primary:hover:not(:disabled) {
  background: #1a5cc7;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.demo-info {
  font-size: 0.85rem;
  color: #888;
  background: #f9f9f9;
  padding: 12px;
  border-radius: 8px;
  margin: 0;
}
</style>
