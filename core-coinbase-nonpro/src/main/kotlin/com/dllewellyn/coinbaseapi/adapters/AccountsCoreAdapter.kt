package com.dllewellyn.coinbaseapi.adapters

import com.dllewellyn.coinbaseapi.http.InternalHttpClient
import com.dllewellyn.coinbaseapi.models.account.Account
import com.dllewellyn.coinbaseapi.models.account.Transaction
import com.dllewellyn.coinbaseapi.models.toCore
import com.dllewellyn.coinbaseapi.nonpro.interfaces.Accounts
import com.dllewellyn.coinbaseapi.nonpro.interfaces.CoreAccounts
import com.dllewellyn.coinbaseapi.nonpro.interfaces.Prices
import com.dllewellyn.denarii.models.currency.CurrencyPair
import com.dllewellyn.denarii.repositories.ReadOnlyRepositoryArgument
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import io.ktor.client.request.delete
import io.ktor.client.request.put
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent

class AccountsCoreAdapter(
    private val accounts: Accounts,
    private val transactionRetriever: ReadOnlyRepositoryArgument<String, List<Transaction>>,
    private val client: InternalHttpClient,
    private val prices: Prices
) : CoreAccounts {

    override suspend fun update(thingToUpdate: Account, newValue: String) {
        client.httpClient.put<Account>(thingToUpdate.uid) {
            body = TextContent("{\"name\": \"$newValue\"}", ContentType.Application.Json)
        }
    }

    override suspend fun delete(toDelete: Account) {
        client.httpClient.delete<HttpResponse>(client.url("accounts/${toDelete.uid}"))
    }

    @ExperimentalUnsignedTypes
    override suspend fun retrieveData() = accounts.getAllNonEmptyAccounts().data.map {
        with(it.toCore()) {
            copy(
                transactions = transactionRetriever.retrieveData(it.id),
                dollarValue = prices.getSpotPrice(CurrencyPair.fromId("${currencyValue.id}-USD")).amount.multiply(
                    BigDecimal.parseString(
                        it.balance.amount
                    )
                )
            )
        }
    }
}