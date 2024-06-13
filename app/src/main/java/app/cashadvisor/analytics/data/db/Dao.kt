package app.cashadvisor.analytics.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.TotalAmountByCategoryIdEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import app.cashadvisor.analytics.data.db.models.CategoryWithUserAnalytics
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    fun upsertUserAnalyticsEntity(userAnalyticsEntity: List<UserAnalyticsEntity>)

    @Upsert
    fun upsertCategoryEntity(categoryEntity: List<CategoryEntity>)

    @Query("DELETE FROM userAnalyticsTable")
    fun removeAllUserAnalyticsTable()

    @Query("DELETE FROM categoryTable")
    fun removeAllCategoryTable()

}