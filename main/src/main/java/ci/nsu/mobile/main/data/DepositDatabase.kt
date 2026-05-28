package ci.nsu.mobile.main.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Меняем version = 2
@Database(entities = [Deposit::class], version = 2, exportSchema = false)
abstract class DepositDatabase : RoomDatabase() {

    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: DepositDatabase? = null

        fun getDatabase(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposit_database"
                )
                    .fallbackToDestructiveMigration() // Позволяет пересоздать БД без ошибок
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}