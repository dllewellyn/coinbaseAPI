package com.dllewellyn.coinbaseapi.models

import com.dllewellyn.coinbaseapi.models.currency.SupportedCurrency
import com.google.gson.annotations.SerializedName


data class ApiCurrencies(
    @SerializedName("data") val data: List<ApiCurrency>
)

data class ApiCurrency(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("min_size") val minSize: String
)

fun ApiCurrency.toCurrency() =
    SupportedCurrency(id, name, minSize.toDouble())