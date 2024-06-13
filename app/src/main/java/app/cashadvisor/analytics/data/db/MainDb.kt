package app.cashadvisor.analytics.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.TotalAmountByCategoryIdEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity

@Database(
    entities = [
        UserAnalyticsEntity::class,
        CategoryEntity::class,
        TotalAmountByCategoryIdEntity::class],
    version = 1
)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        fun getDb(context: Context): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "cashadvisor.db"
            ).build()
        }
    }
}