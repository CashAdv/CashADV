package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.cashadvisor.analytics.data.db.models.Category

@Entity(
    indices = [Index(value = ["date", "type", "planned" ])],
    primaryKeys = ["id", "type"],
    tableName = "userAnalyticsTable",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId")
    )])
data class UserAnalyticsEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "type")
    val type: Category,
    @ColumnInfo(name = "amount")
    val amount: Int,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "planned")
    val planned: Boolean,
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "bankAccount")
    val bankAccount: String,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "title")
    val title: String
)