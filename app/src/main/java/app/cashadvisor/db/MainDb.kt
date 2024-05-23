package app.cashadvisor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.cashadvisor.db.Entities.CategoryEntity
import app.cashadvisor.db.Entities.UserAnalyticsEntity

@Database(entities = [UserAnalyticsEntity::class, CategoryEntity::class], version = 1)
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