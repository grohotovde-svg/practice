package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    fun getDepositsByUser(userId: Int): Flow<List<Deposit>> = dao.getDepositsByUser(userId)

    fun getDepositById(id: Int): Flow<Deposit?> = dao.getDepositById(id)

    suspend fun insert(deposit: Deposit) {
        dao.insert(deposit)
    }

    suspend fun clearHistoryByUser(userId: Int) {
        dao.clearHistoryByUser(userId)
    }
}