package org.example.user_service.utils

object PhoneUtils {
    // Accepts Russian numbers in formats: +7XXXXXXXXXX or 8XXXXXXXXXX or 7XXXXXXXXXX
    private val rusRegex = Regex("""^(?:\+7|7|8)(\d{10})$""")

    /**
     * Normalize to +7XXXXXXXXXX or return null if not Russian phone
     */
    fun normalizeRussianPhone(raw: String): String? {
        val cleaned = raw.replace(Regex("[^0-9+]"), "")
        val m = rusRegex.matchEntire(cleaned) ?: return null
        val digits = m.groupValues[1]
        return "+7$digits"
    }
}