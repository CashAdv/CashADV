package app.cashadvisor.analytics.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity

@Dao
interface Dao {
    @Upsert
    fun upsertUserAnalyticsEntity(userAnalyticsEntity: List<UserAnalyticsEntity>)

    @Upsert
    fun upsertCategoryEntity(categoryEntity: List<CategoryEntity>)

    @Upsert
    fun upsertCategoryWithAnalyticsEntity(categoryEntity: List<CategoryEntity>)

    //Вернуть все записи по выбранной категории
    @Query("SELECT * FROM userAnalyticsTable WHERE userAnalyticsTable.categoryId = :categoryName")
    fun getByCategoryName(categoryName: String): List<CategoryWithUserAnalyticsEntity>

    //Вернуть все записи по выбранной дате
    @Query("SELECT * FROM userAnalyticsTable WHERE date = :date")
    fun getByDate(date: String): List<CategoryWithUserAnalyticsEntity>

    //Вернуть все записи по промежутку дат
    @Query("SELECT * FROM userAnalyticsTable WHERE substr(date,1,length(:dateStart)) BETWEEN :dateStart AND :dateEnd")
    fun getByTowDate(dateStart: String, dateEnd: String): List<CategoryWithUserAnalyticsEntity>

    @Query("DELETE FROM userAnalyticsTable")
    fun removeAllUserAnalyticsTable()

    @Query("DELETE FROM categoryTable")
    fun removeAllCategoryTable()

}