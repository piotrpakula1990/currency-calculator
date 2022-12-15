package com.example.currencycalculator.utils

enum class Currency(val country: String, val flag: String) {
    BDT("Bangladesh", "🇧🇩"),
    EUR("Euro Member Countries", "🇪🇺"),
    USD("United States", "🇺🇲");

    companion object {
        fun getFlag(shortcut: String): String = try {
            valueOf(shortcut).flag
        } catch (e: Exception) {
            ""
        }
    }
}