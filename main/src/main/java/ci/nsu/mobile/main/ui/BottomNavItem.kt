package ci.nsu.mobile.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    // Используем базовую иконку профиля (AccountCircle)
    object Users : BottomNavItem("users_tab", "Пользователи", Icons.Default.AccountCircle)
    // Используем базовую плюсик (Add) вместо калькулятора
    object Calculator : BottomNavItem("calculator_tab", "Новый расчёт", Icons.Default.Add)
    // Используем базовую иконку списка (Menu)
    object History : BottomNavItem("history_tab", "Мои расчёты", Icons.Default.Menu)
}