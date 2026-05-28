package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.Deposit
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.data.TokenManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Теперь это обычная ViewModel, а не AndroidViewModel!
class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    // Достаем ID текущего пользователя
    private val currentUserId: Int
        get() = TokenManager.getCurrentUserId()

    // Загружаем только расчеты текущего юзера
    val userDeposits: StateFlow<List<Deposit>> = flow {
        emitAll(repository.getDepositsByUser(currentUserId))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveDeposit(deposit: Deposit) {
        viewModelScope.launch {
            // Принудительно ставим ID пользователя перед сохранением
            val depositWithUser = deposit.copy(userId = currentUserId)
            repository.insert(depositWithUser)
        }
    }

    fun getDepositById(id: Int): Flow<Deposit?> = repository.getDepositById(id)

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistoryByUser(currentUserId)
        }
    }
}