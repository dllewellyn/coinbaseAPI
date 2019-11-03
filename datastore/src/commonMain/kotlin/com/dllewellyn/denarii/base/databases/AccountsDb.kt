package com.dllewellyn.denarii.base.databases

import com.dllewellyn.denarii.base.BaseDb
import com.dllewellyn.denarii.base.toCore
import com.dllewellyn.denarii.base.toEntity
import com.dllewellyn.coinbaseapi.models.account.Account
import com.dllewellyn.coinbaseapi.repositories.ReadOnlyRepository
import com.dllewellyn.coinbaseapi.repositories.WriteRepository
import com.squareup.sqldelight.db.SqlDriver

class AccountsDb(private val sqlDriver: SqlDriver) : BaseDb<List<Account>>(sqlDriver), ReadOnlyRepository<List<Account>>,
    WriteRepository<List<Account>> {

    override suspend fun write(value: List<Account>) {
        value.forEach { account ->
            database.accountsQueries
                .insertAccount(account.toEntity())

            account.transactions.map { transaction ->
                transaction.toEntity(account.uid)
            }.forEach { database.transactionQueries.insertIntoTransactions(it) }

        }
    }

    override suspend fun retrieveData() =
        database.accountsQueries.selectAllFromAccounts()
            .executeAsList()
            .map {
                it.toCore()
                    .copy(transactions =
                    database.transactionQueries.selectAllTransactionsForAccount(it.uid).executeAsList().map { transaction ->
                        transaction.toCore()
                    })
            }
}