// Modèle d'un livre
export interface Book {
    id?: number
    title: string
    author: string
    isbn: string
    price: number
    description?: string
    category: BookCategory
    publicationYear?: number | null
    coverUrl?: string
}

// Catégories de livres (doivent correspondre au backend)
export enum BookCategory {
    ROMAN = "ROMAN",
    POESIE = "POESIE",
    THEATRE = "THEATRE",
    ESSAI = "ESSAI",
    BIOGRAPHIE = "BIOGRAPHIE",
}

// Labels en français pour l'affichage
export const BookCategoryLabels: Record<BookCategory, string> = {
    [BookCategory.ROMAN]: "Roman",
    [BookCategory.POESIE]: "Poésie",
    [BookCategory.THEATRE]: "Théâtre",
    [BookCategory.ESSAI]: "Essai",
    [BookCategory.BIOGRAPHIE]: "Biographie",
}

// Types pour l'authentification
export interface User {
    username: string
    role: string
}

export interface LoginCredentials {
    username: string
    password: string
}

export interface AuthResponse {
    token: string
    username: string
    role: string
}

// Types pour les avis CV
export interface CvReview {
    id?: number
    authorName: string
    email?: string
    comment: string
    rating: number
    createdAt?: string
}

export interface CvReviewStats {
    averageRating: number
    totalReviews: number
}
