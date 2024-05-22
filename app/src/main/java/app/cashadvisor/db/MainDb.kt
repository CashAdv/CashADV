package app.cashadvisor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Spend::class], version = 1)
abstract class MainDb : RoomDatabase() {

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