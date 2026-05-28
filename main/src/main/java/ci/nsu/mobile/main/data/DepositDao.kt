package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: Deposit)

    // Ищем только расчеты текущего пользователя!
    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY id DESC")
    fun getDepositsByUser(userId: Int): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE id = :id LIMIT 1")
    fun getDepositById(id: Int): Flow<Deposit?>

    // Удаляем только историю текущего пользователя
    @Query("DELETE FROM deposits WHERE userId = :userId")
    suspend fun clearHistoryByUser(userId: Int)
}