package com.example.currencycalculator.utils

enum class Currency(val fullName: String, val flag: String) {
    AUD("Australian Dollar", "🇦🇺"),
    AZN("Azerbaijanian Manat", "🇦🇿"),
    BRL("Brazilian Real", "🇧🇷"),
    CAD("Canadian Dollar", "🇨🇦"),
    CNY("Yuan Renminbi", "🇨🇳"),
    CZK("Czech Koruna", "🇨🇿"),
    DKK("Danish Krone", "🇩🇰"),
    EUR("Euro", "🇪🇺"),
    GEL("Lari", "🇬🇪"),
    HUF("Forint", "🇭🇺"),
    ISK("Iceland Krona", "🇮🇸"),
    INR("Indian Rupee", "🇮🇳"),
    IDR("Rupiah", "🇲🇨"),
    IRR("Iranian Rial", "🇮🇷"),
    ILS("New Israeli Sheqel", "🇮🇱"),
    JPY("Yen", "🇯🇵"),
    MXN("Mexican Peso", "🇲🇽"),
    NOK("Norwegian Krone", "🇧🇻"),
    PLN("Zloty", "🇯🇵"),
    RUB("Russian Ruble", "🇷🇺"),
    RSD("Serbian Dinar", "🇷🇸"),
    CHF("Swiss Franc", "🇨🇭"),
    TRY("Turkish Lira", "🇹🇷"),
    UAH("Hryvnia", "🇺🇦"),
    USD("US Dollar", "🇺🇲");

    companion object {
        fun getFlag(shortcut: String): String = try {
            valueOf(shortcut).flag
        } catch (e: Exception) {
            ""
        }

        fun isExist(shortcut: String): Boolean = try {
            valueOf(shortcut)
            true
        } catch (e: Exception) {
            false
        }
    }
}