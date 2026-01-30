// src/services/api.ts
import axios from 'axios'
import router from '@/router'

const api = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

// INTERCEPTEUR DE REQUÊTE
// Ajoute le token JWT à chaque requête sortante
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            // S'assure que headers existe
            config.headers = config.headers ?? {}
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// INTERCEPTEUR DE RÉPONSE
// Gère les erreurs 401 (non authentifié)
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error?.response?.status === 401) {
            // Token expiré ou invalide
            localStorage.removeItem('token')
            localStorage.removeItem('user')

            // Redirection vers la page de login
            router.push('/login')
        }
        return Promise.reject(error)
    }
)

export default api
