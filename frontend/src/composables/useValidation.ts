// src/composables/useValidation.ts
import { ref, computed, reactive } from 'vue'

// Type pour une regle de validation
type ValidationRule = (value: any) => string | true

// Type pour un ensemble de regles par champ
type ValidationRules<T> = {
    [K in keyof T]?: ValidationRule[]
}

export function useValidation<T extends Record<string, any>>(
    initialData: T,
    rules: ValidationRules<T>
) {
    // Donnees du formulaire
    const formData = reactive({ ...initialData })

    // Erreurs par champ
    const errors = reactive<Record<keyof T, string>>(
        {} as Record<keyof T, string>
    )

    // Champs touches (pour n'afficher les erreurs qu 'apres interaction)
    const touched = reactive<Record<keyof T, boolean>>(
        {} as Record<keyof T, boolean>
    )

    // Valider un champ specifique
    function validateField(field: keyof T): boolean {
        const fieldRules = rules[field] || []

        for (const rule of fieldRules) {
            const result = rule(formData[field])
            if (result !== true) {
                errors[field] = result
                return false
            }
        }

        errors[field] = ''
        return true
    }

    // Valider tous les champs
    function validateAll(): boolean {
        let isValid = true

        for (const field of Object.keys(rules) as (keyof T)[]) {
            touched[field] = true
            if (!validateField(field)) {
                isValid = false
            }
        }

        return isValid
    }

    // Marquer un champ comme touche (sur blur)
    function handleBlur(field: keyof T): void {
        touched[field] = true
        validateField(field)
    }

    // Valider en temps reel (sur input)
    function handleInput(field: keyof T): void {
        if (touched[field]) {
            validateField(field)
        }
    }

    // Reinitialiser le formulaire
    function resetForm(): void {
        Object.assign(formData, initialData)
        for (const key of Object.keys(errors)) {
            errors[key as keyof T] = ''
        }
        for (const key of Object.keys(touched)) {
            touched[key as keyof T] = false
        }
    }

    // Le formulaire est -il valide ?
    const isValid = computed(() => {
        return Object.values(errors).every((e) => !e)
    })

    return {
        formData,
        errors,
        touched,
        validateField,
        validateAll,
        handleBlur,
        handleInput,
        resetForm,
        isValid
    }
}

// Regles de validation reutilisables
export const validationRules = {
    required:
        (message = 'Ce champ est obligatoire') =>
            (value: any) => {
                if (value === null || value === undefined || value === '') {
                    return message
                }
                return true
            },

    minLength:
        (min: number, message?: string) =>
            (value: string) => {
                if (value.length < min) {
                    return message || `Minimum ${min} caracteres`
                }
                return true
            },

    maxLength:
        (max: number, message?: string) =>
            (value: string) => {
                if (value.length > max) {
                    return message || `Maximum ${max} caracteres`
                }
                return true
            },

    isbn:
        (message = 'Format ISBN invalide (978-XXXXXXXXXX)') =>
            (value: string) => {
                const isbnRegex = /^978-\d{10}$/
                if (!isbnRegex.test(value)) {
                    return message
                }
                return true
            },

    positiveNumber:
        (message = 'Doit etre un nombre positif') =>
            (value: number) => {
                if (typeof value !== 'number' || value <= 0) {
                    return message
                }
                return true
            },

    year:
        (min: number, max: number) =>
            (value: number) => {
                if (value < min || value > max) {
                    return `Annee entre ${min} et ${max}`
                }
                return true
            }
}
