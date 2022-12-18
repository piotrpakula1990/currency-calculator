package com.example.currencycalculator.utils

import androidx.annotation.StringRes
import com.example.currencycalculator.R

enum class Currency(@StringRes val fullNameId: Int, val flag: String) {
    AUD(R.string.AUD, "🇦🇺"),
    AZN(R.string.AZN, "🇦🇿"),
    BRL(R.string.BRL, "🇧🇷"),
    CAD(R.string.CAD, "🇨🇦"),
    CNY(R.string.CNY, "🇨🇳"),
    CZK(R.string.CZK, "🇨🇿"),
    DKK(R.string.DKK, "🇩🇰"),
    EUR(R.string.EUR, "🇪🇺"),
    GEL(R.string.GEL, "🇬🇪"),
    HUF(R.string.HUF, "🇭🇺"),
    ISK(R.string.ISK, "🇮🇸"),
    INR(R.string.INR, "🇮🇳"),
    IDR(R.string.IDR, "🇲🇨"),
    IRR(R.string.IRR, "🇮🇷"),
    ILS(R.string.ILS, "🇮🇱"),
    JPY(R.string.JPY, "🇯🇵"),
    MXN(R.string.MXN, "🇲🇽"),
    NOK(R.string.NOK, "🇧🇻"),
    PLN(R.string.PLN, "🇵🇱"),
    RUB(R.string.RUB, "🇷🇺"),
    RSD(R.string.RSD, "🇷🇸"),
    CHF(R.string.CHF, "🇨🇭"),
    TRY(R.string.TRY, "🇹🇷"),
    UAH(R.string.UAH, "🇺🇦"),
    USD(R.string.USD, "🇺🇲");

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