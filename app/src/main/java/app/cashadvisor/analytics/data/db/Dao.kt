package app.cashadvisor.analytics.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    suspend fun upsertUserAnalyticsEntity(userAnalyticsEntity: List<UserAnalyticsEntity>)

    @Upsert
    suspend fun upsertCategoryEntity(categoryEntity: List<CategoryEntity>)

    @Upsert
    suspend fun upsertCategoryWithAnalyticsEntity(categoryEntity: List<CategoryWithUserAnalyticsEntity>)

    @Transaction
    @Query("SELECT userAnalyticsTable.id as userAnalyticsId," +
            "categoryTable.id as categoryId," +
            "userAnalyticsTable.planned," +
            "name as categoryName, icon as categoryIcon," +
            "userAnalyticsTable.amount," +
            "userAnalyticsTable.currency, " +
            "userAnalyticsTable.date as date " +
            "FROM userAnalyticsTable, categoryTable")
    suspend fun getCategoryWithUserAnalyticsEntityList(): List<CategoryWithUserAnalyticsEntity>

    //Вернуть все записи по выбранной категории
    @Query("SELECT * FROM analyticsWithCategory WHERE analyticsWithCategory.categoryName = :categoryName")
    suspend fun getByCategoryName(categoryName: String): List<CategoryWithUserAnalyticsEntity>

    //Вернуть все записи по выбранной дате
    @Query("SELECT * FROM analyticsWithCategory WHERE date = :date")
    suspend fun getByDate(date: String): List<CategoryWithUserAnalyticsEntity>

    //Вернуть все записи по промежутку дат
    @Query("SELECT * FROM analyticsWithCategory WHERE substr(date,1,length(:dateStart)) BETWEEN :dateStart AND :dateEnd")
    suspend fun getByTwoDate(dateStart: String, dateEnd: String): List<CategoryWithUserAnalyticsEntity>

    @Query("SELECT * FROM analyticsWithCategory WHERE amount BETWEEN :minAmount AND :maxAmount")
    suspend fun getByTwoAmount(minAmount: Int, maxAmount: Int): List<CategoryWithUserAnalyticsEntity>

    @Query("SELECT * FROM analyticsWithCategory WHERE amount >= :minAmount")
    suspend fun getMoreThenMinAmount(minAmount: Int): List<CategoryWithUserAnalyticsEntity>


    @Query("DELETE FROM userAnalyticsTable")
    fun removeAllUserAnalyticsTable()

    @Query("DELETE FROM categoryTable")
    fun removeAllCategoryTable()
}