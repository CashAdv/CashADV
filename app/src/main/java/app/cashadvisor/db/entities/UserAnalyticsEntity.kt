package app.cashadvisor.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userAnalyticsTable")
data class UserAnalyticsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "userId")
    var userId: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "amount")
    var amount: Int,
    @ColumnInfo(name = "category")
    var categoryId: Int,
    @ColumnInfo(name = "planned")
    var planned: Boolean,
    @ColumnInfo(name = "currency")
    var currency: String,
    @ColumnInfo(name = "bankAccount")
    var bankAccount: String
)