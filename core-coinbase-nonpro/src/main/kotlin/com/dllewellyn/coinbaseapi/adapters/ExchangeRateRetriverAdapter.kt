package com.dllewellyn.coinbaseapi.adapters

import com.dllewellyn.coinbaseapi.interfaces.ExchangeRateRetriver
import com.dllewellyn.coinbaseapi.models.ApiCurrencyRates
import com.dllewellyn.coinbaseapi.models.ExchangeRates
import com.dllewellyn.coinbaseapi.models.currency.CryptoCurrency
import io.ktor.client.request.get
import java.math.BigDecimal


class ExchangeRateRetriverAdapter(private val httpClient: InternalHttpClient) :
    ExchangeRateRetriver() {

    override suspend fun retrieveData(arg: CryptoCurrency) =
        httpClient.httpClient.get<ApiCurrencyRates>("${httpClient.url("exchange-rates")}?=${arg.str}")
            .let {
                ExchangeRates(
                    arg,
                    it.data.rates.mapValues { rate -> BigDecimal(rate.value) })
            }

}