<template>
  <header class="nav">
    <div class="inner">
      <RouterLink class="brand" to="/">Les Belles Lettres</RouterLink>

      <nav class="links">
        <RouterLink to="/books">Catalogue</RouterLink>

        <RouterLink to="/cv">CV</RouterLink>

        <RouterLink v-if="auth.isAdmin" to="/admin">Admin</RouterLink>

        <RouterLink v-if="!auth.isAuthenticated" to="/login">Login</RouterLink>

        <button v-else class="logout" @click="onLogout">
          Déconnexion ({{ auth.username }})
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function onLogout() {
  auth.logout()
  router.push('/')
}
</script>

<style scoped>
.nav {
  width: 100%;
  border-bottom: 1px solid #e6e6e6;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.inner {
  width: min(1000px, 92%);
  margin: 0 auto;
  padding: 12px 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
}
.brand {
  font-weight: 800;
  text-decoration: none;
  color: #111;
}
.links {
  display: flex;
  align-items: center;
  gap: 12px;
}
.links a {
  text-decoration: none;
  color: #1f6feb;
  font-weight: 600;
}
.logout {
  border: none;
  background: #eee;
  padding: 8px 10px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 700;
}
</style>
