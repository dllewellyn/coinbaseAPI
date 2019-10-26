package com.dllewellyn.coinbaseapi

import com.dllewellyn.coinbaseapi.models.account.Account
import com.dllewellyn.coinbaseapi.multiplatform.databases.AccountsDb
import com.dllewellyn.coinbaseapi.retrievers.CachingRepository
import com.dllewellyn.coinbaseapi.retrievers.CompositeRetriever
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        with(CoinbaseApi()) {


            val api = ApikeyCoinbaseApi(System.getenv("COINBASE_KEY"), System.getenv("COINBASE_SECRET"))
            val remote = CompositeRetriever<Account>().apply {
                retrievers.add(api.coreAccounts())
            }

            remote.retrieveData().forEach {
                println(it)
            }

//            val local = AccountsDb()
//
//            val cachingRepository = CachingRepository(remote, local, local)
//            cachingRepository.initialise()
//            cachingRepository.refresh()
        }
    }
}