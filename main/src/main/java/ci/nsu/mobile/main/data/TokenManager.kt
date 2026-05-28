package ci.nsu.mobile.main.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            prefs.edit().putString(TOKEN_KEY, value).apply()
        }

    fun clear() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    // НОВАЯ ФУНКЦИЯ: Достаем ID пользователя из JWT
    fun getCurrentUserId(): Int {
        val currentToken = token ?: return -1
        return try {
            val payload = currentToken.split(".")[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val json = JSONObject(String(decodedBytes, charset("UTF-8")))
            // Ищем поле userId (если у вас в бэкенде оно называется иначе, например "id", поменяйте здесь)
            json.optInt("userId", 1)
        } catch (e: Exception) {
            1 // Значение по умолчанию, если что-то пошло не так
        }
    }
}