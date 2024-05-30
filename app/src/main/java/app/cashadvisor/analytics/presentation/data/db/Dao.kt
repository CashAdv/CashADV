package app.cashadvisor.analytics.presentation.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.cashadvisor.analytics.presentation.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.presentation.data.db.entities.UserAnalyticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    fun upsertUserAnalyticsEntity(userAnalyticsEntity: List<UserAnalyticsEntity>)

    @Upsert
    fun upsertCategoryEntity(categoryEntity: List<CategoryEntity>)

    @Query("SELECT * FROM categoryTable")
    fun getCategoryWithUserAnalytics(): Flow<List<CategoryWithUserAnalytics>>

    @Query("DELETE FROM userAnalyticsTable")
    fun deleteAllUserAnalyticsEntity()
}