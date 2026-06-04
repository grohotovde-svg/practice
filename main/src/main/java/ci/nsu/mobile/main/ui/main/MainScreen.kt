package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.clickable // НОВЫЙ ИМПОРТ
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onLogout: () -> Unit,
    onUserClick: (Int) -> Unit // НОВЫЙ ПАРАМЕТР: обработчик клика по пользователю
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(uiState.users) { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // Важно, чтобы вся ширина была кликабельной
                            .clickable { onUserClick(user.id) } // ДОБАВЛЕНИЕ ОБРАБОТЧИКА КЛИКА
                            .padding(16.dp)
                    ) {
                        Text(user.login, style = MaterialTheme.typography.bodyLarge)
                        Text(user.email, style = MaterialTheme.typography.bodyMedium)
                    }
                    Divider()
                }

                uiState.error?.let { error ->
                    item {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}