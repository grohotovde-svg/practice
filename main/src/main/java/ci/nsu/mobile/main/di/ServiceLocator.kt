package ci.nsu.mobile.main.di

import android.content.Context
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.DepositDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.data.network.RetrofitClient

class ServiceLocator(private val context: Context) {

    val database: DepositDatabase by lazy {
        DepositDatabase.getDatabase(context)
    }


    val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitClient.instance)
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepository(database.depositDao())
    }
}