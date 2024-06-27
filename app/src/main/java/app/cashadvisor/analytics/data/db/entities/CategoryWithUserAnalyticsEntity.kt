package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import app.cashadvisor.analytics.data.db.models.Category

@Entity(
    indices = [Index(value = ["date", "categoryName", "type", "planned"])],
    primaryKeys = ["userAnalyticsId", "categoryId"],
    tableName = "analyticsWithCategory"
)
data class CategoryWithUserAnalyticsEntity(
    @ColumnInfo(name = "userAnalyticsId")
    val userAnalyticsId: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "type")
    val type: Category,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "planned")
    val planned: Boolean,
    @ColumnInfo(name = "categoryName")
    val categoryName: String,
    @ColumnInfo(name = "categoryIcon")
    val categoryIcon: String,
    @ColumnInfo(name = "amount")
    val amount: Int,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "date")
    val date: String
)