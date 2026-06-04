package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.data.models.*
import ci.nsu.mobile.main.data.network.ApiService // <-- Убедитесь, что импорт правильный

class AuthRepository(private val apiService: ApiService) {

    suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            apiService.register(registerRequest)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(login: String, password: String): Result<UserDto?> {
        return try {
            val response = apiService.login(LoginRequest(login, password))
            TokenManager.token = response.token
            Result.success(null) // Здесь был null, возможно, вы хотите вернуть UserDto?
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val groups = apiService.getGroups()
            Result.success(groups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // НОВЫЙ МЕТОД: Получение пользователя по ID
    suspend fun getUserById(userId: Int): Result<UserDto> {
        return try {
            val user = apiService.getUserById(userId)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}