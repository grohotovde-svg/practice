package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShoppingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val currentText = _uiState.value.newItemText
        if (currentText.isNotBlank()) {
            _uiState.update { currentState ->
                val newItem = ShoppingItem(
                    // УЛУЧШЕНИЕ: Ищем максимальный id и добавляем 1.
                    // Это защищает от дубликатов id после удаления элементов.
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
                if (item.id == itemId) {
                    item.copy(isBought = !item.isBought)
                } else {
                    item
                }
            }
            currentState.copy(items = updatedItems)
        }
    }

    // --- РЕАЛИЗАЦИЯ ---
    fun deleteItem(itemId: Int) {
        _uiState.update { currentState ->
            // Создаем новый список, отфильтровав все элементы,
            // кроме того, у которого id совпадает с удаляемым.
            val updatedItems = currentState.items.filterNot { it.id == itemId }
            currentState.copy(items = updatedItems)
        }
    }
}