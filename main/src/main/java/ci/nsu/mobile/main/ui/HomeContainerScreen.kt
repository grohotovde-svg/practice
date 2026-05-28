package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.AppViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.screens.Step1Screen
import ci.nsu.mobile.main.ui.main.MainViewModel

@Composable
fun HomeContainerScreen(
    factory: AppViewModelFactory,
    onLogout: () -> Unit,
    onNavigateToResult: (Double, Int) -> Unit,
    onNavigateToHistoryDetail: (Int) -> Unit
) {
    val bottomNavController = rememberNavController()

    val items = listOf(
        BottomNavItem.Users,
        BottomNavItem.Calculator,
        BottomNavItem.History
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                bottomNavController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Users.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Вкладка 1: Список пользователей
            composable(BottomNavItem.Users.route) {
                val mainVm: MainViewModel = viewModel(factory = factory)
                MainScreen(viewModel = mainVm, onLogout = onLogout)
            }

            // Вкладка 2: Новый расчет (Шаг 1 калькулятора)
            composable(BottomNavItem.Calculator.route) {
                Step1Screen(
                    onBackClick = {
                        bottomNavController.navigate(BottomNavItem.Users.route) {
                            popUpTo(BottomNavItem.Users.route) { inclusive = true }
                        }
                    },
                    onNextClick = { startAmount, months ->
                        onNavigateToResult(startAmount, months)
                    }
                )
            }

            // Вкладка 3: История расчетов
            composable(BottomNavItem.History.route) {
                val depositVm: DepositViewModel = viewModel(factory = factory)
                HistoryScreen(
                    viewModel = depositVm,
                    onBackClick = {
                        bottomNavController.navigate(BottomNavItem.Users.route) {
                            popUpTo(BottomNavItem.Users.route) { inclusive = true }
                        }
                    },
                    onItemClick = { deposit ->
                        onNavigateToHistoryDetail(deposit.id)
                    }
                )
            }
        }
    }
}