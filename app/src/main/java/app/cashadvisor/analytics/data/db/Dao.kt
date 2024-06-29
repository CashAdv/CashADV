package app.cashadvisor.analytics.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import app.cashadvisor.analytics.data.db.models.SumByCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Upsert
    suspend fun upsertUserAnalyticsEntity(userAnalyticsEntity: UserAnalyticsEntity)

    @Upsert
    suspend fun upsertCategoryEntity(categoryEntity: CategoryEntity)

    @Query("SELECT categoryId, title, SUM(amount) as sum, categoryTable.name as categoryName, categoryTable.icon as categoryIcon " +
            "FROM categoryTable, userAnalyticsTable " +
            "WHERE type = :type " +
            "AND date >= :dateStart AND date <= :dateEnd " +
            "AND planned = :isPlanned " +
            "GROUP BY categoryId, title " +
            "ORDER BY SUM(amount)")
    fun getSumAmountByCategory(type: String, dateStart: Long, dateEnd: Long, isPlanned: Boolean): Flow<List<SumByCategory>>

    @Query("DELETE FROM userAnalyticsTable")
    fun removeAllUserAnalyticsTable()

    @Query("DELETE FROM categoryTable")
    fun removeAllCategoryTable()
}