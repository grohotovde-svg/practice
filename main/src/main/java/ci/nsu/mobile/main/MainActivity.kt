package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme   // если функция в Theme.kt называется иначе — поправь здесь

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                val shoppingViewModel: ShoppingViewModel = viewModel()
                ShoppingScreen(viewModel = shoppingViewModel)
            }
        }
    }
}



@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastMessages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.newItemText,
                    onValueChange = { viewModel.onNewItemTextChanged(it) },
                    label = { Text("Что купить?") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.addItem() },
                    enabled = uiState.newItemText.isNotBlank()
                ) {
                    Text("Добавить")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    ShoppingListItem(
                        item = item,
                        onToggleBought = { viewModel.toggleItemBought(item.id) },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onToggleBought: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isBought,
            onCheckedChange = { onToggleBought() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.name,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isBought) TextDecoration.LineThrough else null,
            color = if (item.isBought)
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            else
                MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить товар",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}