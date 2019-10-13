package com.dllewellyn.coinbaseapi.multiplatform

import com.dllewellyn.coinbaseapi.AccountEntity
import com.dllewellyn.coinbaseapi.ProductTickerEntity
import com.dllewellyn.coinbaseapi.models.Account
import com.dllewellyn.coinbaseapi.models.currency.CurrencyValue
import com.dllewellyn.coinbaseapi.models.currency.SupportedCurrency
import com.dllewellyn.coinbaseapi.models.marketinfo.ProductTicker
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import java.math.BigDecimal

 fun retrieveDatabase(): SqlDriver = JdbcSqliteDriver(IN_MEMORY)

fun ProductTickerEntity.toCore() = ProductTicker(
    BigDecimal(ask),
    BigDecimal(bid),
    BigDecimal(price),
    size,
    time,
    tradeId.toInt(),
    volume
)

fun ProductTicker.toEntity(): ProductTickerEntity = ProductTickerEntity.Impl(
    ask.toPlainString(),
    bid.toPlainString(),
    price.toPlainString(),
    size,
    time,
    tradeId.toLong(),
    volume
)

fun Account.toEntity() : AccountEntity = AccountEntity.Impl(
    currencyValue.id,
    balance.toPlainString(),
    available?.toPlainString() ?: "",
    hold?.toPlainString() ?: "",
    uid,
    provider
)

fun AccountEntity.toCore() = Account(
    SupportedCurrency(currencyValue),
    BigDecimal(balance),
    if (available == "") {
        null
    } else {
        BigDecimal(available)
    },
    if (hold == "") {
        null
    } else {
        BigDecimal(hold)
    },
    uid,
    provider
)