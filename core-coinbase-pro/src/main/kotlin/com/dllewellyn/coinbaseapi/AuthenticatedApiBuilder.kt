package com.dllewellyn.coinbaseapi

import com.dllewellyn.coinbaseapi.adapter.AccountsAdapter
import com.dllewellyn.coinbaseapi.adapter.OrdersAdapter
import com.dllewellyn.coinbaseapi.api.models.ApiKeyAuth
import com.dllewellyn.coinbaseapi.exceptions.InvalidConfigurationException
import com.dllewellyn.coinbaseapi.interfaces.Accounts
import com.dllewellyn.coinbaseapi.interfaces.Orders

interface AuthenticatedApi {
    fun accounts(): Accounts
    fun orders(): Orders
}

class AuthenticatedApiBuilder {
    var secretKey: String? = null
    var apiKey: String? = null
    var password: String? = null
    var sandbox = false

    @Throws(InvalidConfigurationException::class)
    fun build() =
        AuthenticatedApiImpl(
            ApiKeyAuth(
                nullOrException(password, ::throwException),
                nullOrException(apiKey, ::throwException),
                nullOrException(secretKey, ::throwException)
            ),
            sandbox
        )

    private fun throwException() = InvalidConfigurationException()

    private fun <T> nullOrException(any: T?, exception: () -> Throwable): T {
        return any ?: throw exception()
    }

}

fun authenticated_builder(block: AuthenticatedApiBuilder.() -> Unit) = AuthenticatedApiBuilder().apply {
    block()
}


class AuthenticatedApiImpl(
    private val apiKeyAuth: ApiKeyAuth,
    private val sandbox: Boolean = false
) : AuthenticatedApi {

    private val retrofit: CoinbaseProService by lazy {
        RetrofitCoroutinesBuilder(sandbox)
            .getProApiAuthentication(apiKeyAuth)
    }

    override fun orders(): Orders = OrdersAdapter(retrofit)
    override fun accounts(): Accounts = AccountsAdapter(retrofit)
}
