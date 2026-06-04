package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.login.LoginViewModel
import ci.nsu.mobile.main.ui.main.MainViewModel
import ci.nsu.mobile.main.ui.register.RegisterViewModel
import ci.nsu.mobile.main.ui.userdetail.UserDetailsViewModel // НОВЫЙ ИМПОРТ

class AppViewModelFactory(
    private val serviceLocator: ServiceLocator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(serviceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(serviceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(serviceLocator.authRepository) as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                DepositViewModel(serviceLocator.depositRepository) as T
            }
            // НОВЫЙ ENTRY: UserDetailsViewModel
            modelClass.isAssignableFrom(UserDetailsViewModel::class.java) -> {
                UserDetailsViewModel(serviceLocator.authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}