package com.dllewellyn.coinbaseapi.models

import com.dllewellyn.coinbaseapi.models.currency.CurrencyValue
import com.dllewellyn.coinbaseapi.models.trade.CurrencyBuyAndSell
import java.lang.IllegalArgumentException


enum class BuyOrSell(val v: String) {
    BUY("buy"),
    SPOT("spot"),
    SELL("sell");

    companion object {
        fun fromString(v: String) = when (v) {
            "buy" -> BUY
            "sell" -> SELL
            "spot" -> SPOT
            else -> throw IllegalArgumentException()
        }
    }
}

sealed class EventResponse {
    data class Level2Snapshot(val buyAndSell: CurrencyBuyAndSell) : EventResponse()
    data class Level2Update(val buyAndSell: CurrencyValue, val buyOrSell: BuyOrSell, val size: Double) : EventResponse()
}

fun <T> Any.only(): List<T> = listOf(this as T)
fun <T> Any.onlyMutable(): MutableList<T> = mutableListOf(this as T)