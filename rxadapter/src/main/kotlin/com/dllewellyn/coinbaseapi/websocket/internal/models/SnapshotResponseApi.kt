package com.dllewellyn.coinbaseapi.websocket.internal.models

import com.dllewellyn.coinbaseapi.models.trade.CurrencyBuyAndSell
import com.dllewellyn.coinbaseapi.models.EventResponse
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class SnapshotResponseApi(
    @SerializedName("asks") val asks: List<List<String>>,
    @SerializedName("bids") val bids: List<List<String>>,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("type") val type: String
) {
    fun toCore(): EventResponse.Level2Snapshot {

        return EventResponse.Level2Snapshot(
            CurrencyBuyAndSell(
                product_id.productIdToPair().first(),
                product_id.productIdToPair().last(),
                BigInteger(bids.first().first()),
                BigInteger(asks.first().first())
            )
        )
    }
}
