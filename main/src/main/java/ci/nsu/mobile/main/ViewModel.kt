package ci.nsu.mobile.main


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ShoppingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    private val _toastMessages = MutableSharedFlow<String>()
    val toastMessages = _toastMessages.asSharedFlow()
    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val currentText = _uiState.value.newItemText
        for (item in _uiState.value.items)
        {
            if (currentText.lowercase().trim() == item.name.lowercase().trim()){
                viewModelScope.launch {
                    _toastMessages.emit("Такой товар уже есть")
                }

                return
            }

        }
        if (currentText.isNotBlank()) {
            _uiState.update { currentState ->
                val newItem = ShoppingItem(

                    id = (currentState.items.maxOfOrNull { it.id } ?: 0) + 1,
                    name = currentText
                )
                currentState.copy(
                    items = currentState.items + newItem,
                    newItemText = ""
                )
            }
        }
    }

    fun toggleItemBought(itemId: Int) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map { item ->
                if (item.id == itemId) item.copy(isBought = !item.isBought)
                else item
            }
            currentState.copy(items = updatedItems)
        }
    }

    fun deleteItem(itemId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                items = currentState.items.filterNot { it.id == itemId }
            )
        }
    }
}