package com.example.data.models

import androidx.annotation.StringRes
import com.example.data.R

enum class Currency(@StringRes val fullNameId: Int, val flag: String) {
    AUD(R.string.AUD, "đŚđş"),
    AZN(R.string.AZN, "đŚđż"),
    BRL(R.string.BRL, "đ§đˇ"),
    CAD(R.string.CAD, "đ¨đŚ"),
    CNY(R.string.CNY, "đ¨đł"),
    CZK(R.string.CZK, "đ¨đż"),
    DKK(R.string.DKK, "đŠđ°"),
    EUR(R.string.EUR, "đŞđş"),
    GBP(R.string.GBP, "đŹđ§"),
    GEL(R.string.GEL, "đŹđŞ"),
    HUF(R.string.HUF, "đ­đş"),
    ISK(R.string.ISK, "đŽđ¸"),
    INR(R.string.INR, "đŽđł"),
    IDR(R.string.IDR, "đ˛đ¨"),
    IRR(R.string.IRR, "đŽđˇ"),
    ILS(R.string.ILS, "đŽđą"),
    JPY(R.string.JPY, "đŻđľ"),
    MXN(R.string.MXN, "đ˛đ˝"),
    NOK(R.string.NOK, "đ§đť"),
    PLN(R.string.PLN, "đľđą"),
    RUB(R.string.RUB, "đˇđş"),
    RSD(R.string.RSD, "đˇđ¸"),
    CHF(R.string.CHF, "đ¨đ­"),
    TRY(R.string.TRY, "đšđˇ"),
    UAH(R.string.UAH, "đşđŚ"),
    USD(R.string.USD, "đşđ˛"),
    UNK(R.string.UNK, "đ´ó ˘ó łó ˘ó °ó ż"); // Unknown

    companion object {

        fun getOrNull(shortcut: String): Currency? = try {
            valueOf(shortcut)
        } catch (e: Exception) {
            null
        }

        fun getAll(): Array<Currency> = values().filter { it != UNK }.toTypedArray()
    }
}