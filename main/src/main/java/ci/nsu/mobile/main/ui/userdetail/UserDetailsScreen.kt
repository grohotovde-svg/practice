package ci.nsu.mobile.main.ui.userdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userId: Int,
    factory: AppViewModelFactory, // Передаем factory для создания ViewModel
    onBackClick: () -> Unit
) {
    val viewModel: UserDetailsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    // Загружаем пользователя при первом показе экрана или изменении userId
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали пользователя", color = Color(0xFF1565C0)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "Неизвестная ошибка",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                uiState.user != null -> {
                    val user = uiState.user!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = user.login,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1565C0),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                DetailRow("ID пользователя:", user.id.toString())
                                DetailRow("Логин:", user.login)
                                DetailRow("Email:", user.email)
                                DetailRow("Телефон:", user.phoneNumber ?: "Не указан")
                                DetailRow("ID роли:", user.roleId.toString())
                                DetailRow("Разрешена авторизация:", if (user.authAllowed) "Да" else "Нет")
                                DetailRow("ID персоны:", user.personId.toString())
                                DetailRow("Дата создания:", user.createdDate)
                                DetailRow("Дата последнего входа:", user.lastLoginDate ?: "Нет данных")
                                // Можно добавить больше полей из UserDto или PersonDto, если нужно
                            }
                        }
                    }
                }
                else -> {
                    // Если user == null и нет ошибки (может быть, ещё не загрузилось или пользователь не найден)
                    Text(
                        text = "Пользователь не найден",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.6f)
        )
    }
}