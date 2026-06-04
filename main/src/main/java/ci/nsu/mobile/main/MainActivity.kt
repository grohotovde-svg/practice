package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.HomeContainerScreen
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.login.LoginScreen
import ci.nsu.mobile.main.ui.login.LoginViewModel
import ci.nsu.mobile.main.ui.register.RegisterScreen
import ci.nsu.mobile.main.ui.register.RegisterViewModel
import ci.nsu.mobile.main.ui.screens.HistoryDetailScreen
import ci.nsu.mobile.main.ui.screens.ResultScreen
import ci.nsu.mobile.main.ui.screens.Step2Screen
import ci.nsu.mobile.main.ui.userdetail.UserDetailsScreen // НОВЫЙ ИМПОРТ
// import ci.nsu.mobile.main.ui.userdetail.UserDetailsViewModel // ViewModel не нужна здесь напрямую

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as MyApplication
        val factory = AppViewModelFactory(app.serviceLocator)

        setContent {
            androidx.compose.material3.MaterialTheme {
                val mainNavController = rememberNavController()

                NavHost(
                    navController = mainNavController,
                    startDestination = "login"
                ) {
                    // ЭКРАН ЛОГИНА
                    composable("login") {
                        val vm: LoginViewModel = viewModel(factory = factory)
                        LoginScreen(
                            viewModel = vm,
                            onLoginSuccess = {
                                mainNavController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { mainNavController.navigate("register") }
                        )
                    }

                    // ЭКРАН РЕГИСТРАЦИИ
                    composable("register") {
                        val vm: RegisterViewModel = viewModel(factory = factory)
                        RegisterScreen(
                            viewModel = vm,
                            onRegistrationSuccess = {
                                mainNavController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateBack = { mainNavController.popBackStack() }
                        )
                    }

                    // ГЛАВНЫЙ ЭКРАН С НИЖНИМ МЕНЮ
                    composable("home") {
                        HomeContainerScreen(
                            factory = factory,
                            onLogout = {
                                mainNavController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onNavigateToResult = { startAmount, months ->
                                mainNavController.navigate("step2/$startAmount/$months")
                            },
                            onNavigateToHistoryDetail = { id ->
                                mainNavController.navigate("history_detail/$id")
                            },
                            onNavigateToUserDetails = { userId -> // НОВЫЙ ПАРАМЕТР ДЛЯ НАВИГАЦИИ К ДЕТАЛЯМ ПОЛЬЗОВАТЕЛЯ
                                mainNavController.navigate("user_details/$userId")
                            }
                        )
                    }

                    // ШАГ 2 КАЛЬКУЛЯТОРА
                    composable(
                        route = "step2/{startAmount}/{months}",
                        arguments = listOf(
                            navArgument("startAmount") { type = NavType.FloatType },
                            navArgument("months") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val startAmount = backStackEntry.arguments?.getFloat("startAmount")?.toDouble() ?: 0.0
                        val months = backStackEntry.arguments?.getInt("months") ?: 0
                        Step2Screen(
                            startAmount = startAmount,
                            months = months,
                            onBackClick = { mainNavController.popBackStack() },
                            onCalculateClick = { rate, monthlyAddition ->
                                mainNavController.navigate("result/$startAmount/$months/$rate/$monthlyAddition")
                            }
                        )
                    }

                    // РЕЗУЛЬТАТ КАЛЬКУЛЯТОРА
                    composable(
                        route = "result/{startAmount}/{months}/{rate}/{monthlyAddition}",
                        arguments = listOf(
                            navArgument("startAmount") { type = NavType.FloatType },
                            navArgument("months") { type = NavType.IntType },
                            navArgument("rate") { type = NavType.FloatType },
                            navArgument("monthlyAddition") { type = NavType.FloatType }
                        )
                    ) { backStackEntry ->
                        val args = backStackEntry.arguments!!
                        val depositVm: DepositViewModel = viewModel(factory = factory)
                        ResultScreen(
                            startAmount = args.getFloat("startAmount").toDouble(),
                            months = args.getInt("months"),
                            rate = args.getFloat("rate").toDouble(),
                            monthlyAddition = args.getFloat("monthlyAddition").toDouble(),
                            viewModel = depositVm,
                            onHomeClick = {
                                mainNavController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    // ДЕТАЛИ ИСТОРИИ
                    composable("history_detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStackEntry ->
                        val depositVm: DepositViewModel = viewModel(factory = factory)
                        HistoryDetailScreen(
                            id = backStackEntry.arguments?.getInt("id") ?: 0,
                            viewModel = depositVm,
                            onBackClick = { mainNavController.popBackStack() }
                        )
                    }

                    // НОВЫЙ МАРШРУТ: Детали пользователя
                    composable(
                        route = "user_details/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                        UserDetailsScreen(
                            userId = userId,
                            factory = factory, // Передаем factory
                            onBackClick = { mainNavController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}