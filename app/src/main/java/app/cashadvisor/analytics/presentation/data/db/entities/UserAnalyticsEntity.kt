package app.cashadvisor.analytics.presentation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userAnalyticsTable")
data class UserAnalyticsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
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
    var comment: String
)