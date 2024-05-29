package app.cashadvisor.analytics.presentation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.cashadvisor.analytics.presentation.data.db.Category

@Entity(tableName = "userAnalyticsTable")
data class UserAnalyticsEntity(
    @PrimaryKey
    var id: String,
    @ColumnInfo(name = "amount")
    var amount: Int,
    @ColumnInfo(name = "categoryId")
    var categoryId: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "planned")
    var planned: Boolean,
    @ColumnInfo(name = "userId")
    var userId: String,
    @ColumnInfo(name = "bankAccount")
    var bankAccount: String,
    @ColumnInfo(name = "currency")
    var currency: String,
    @ColumnInfo(name = "comment")
    var comment: String,
    @ColumnInfo(name = "category")
    var category: Category
)