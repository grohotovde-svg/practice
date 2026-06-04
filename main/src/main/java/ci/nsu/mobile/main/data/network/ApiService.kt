// Пример содержимого ApiService.kt, если он у вас выглядит похоже
package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.LoginRequest
import ci.nsu.mobile.main.data.models.LoginResponse
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.models.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("groups")
    suspend fun getGroups(): List<GroupDto>

    // НОВЫЙ МЕТОД: Получение пользователя по ID
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): UserDto
}