import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api'
import type { User, LoginCredentials, AuthResponse } from '@/types'

export const useAuthStore = defineStore('auth', () => {
    // =====================
    // STATE
    // =====================
    const token = ref<string | null>(localStorage.getItem('token'))
    const user = ref<User | null>(
        JSON.parse(localStorage.getItem('user') || 'null')
    )

    // =====================
    // GETTERS
    // =====================
    const isAuthenticated = computed(() => !!token.value)
    const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')
    const username = computed(() => user.value?.username || '')

    // =====================
    // ACTIONS
    // =====================

    // Connexion
    async function login(credentials: LoginCredentials): Promise<void> {
        const response = await api.post<AuthResponse>(
            '/auth/login',
            credentials
        )

        const data = response.data

        // Mise à jour du state
        token.value = data.token
        user.value = {
            username: data.username,
            role: data.role
        }

        // Persistance
        localStorage.setItem('token', data.token)
        localStorage.setItem('user', JSON.stringify(user.value))
    }

    // Déconnexion
    function logout(): void {
        token.value = null
        user.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('user')
    }

    // Vérification de l’authentification (token encore valide)
    async function checkAuth(): Promise<boolean> {
        if (!token.value) return false

        try {
            const response = await api.get<User>('/auth/me')
            user.value = response.data
            localStorage.setItem('user', JSON.stringify(user.value))
            return true
        } catch {
            logout()
            return false
        }
    }

    return {
        // State
        token,
        user,

        // Getters
        isAuthenticated,
        isAdmin,
        username,

        // Actions
        login,
        logout,
        checkAuth
    }
})
