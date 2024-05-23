package app.cashadvisor.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.cashadvisor.db.Entities.UserAnalyticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    fun upsertUserAnalyticsEntity(userAnalyticsEntity: List<UserAnalyticsEntity>)

    @Query("SELECT * FROM userAnalyticsTable")
    fun getAllUserAnalyticsEntity(): Flow<List<UserAnalyticsEntity>>

    @Query("DELETE FROM userAnalyticsTable")
    fun deleteAllUserAnalyticsEntity()
}