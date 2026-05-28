package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.di.ServiceLocator

class MyApplication : Application() {

    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()

        TokenManager.init(this)

        // Инициализируем локатор сервисов
        serviceLocator = ServiceLocator(this)
    }
}