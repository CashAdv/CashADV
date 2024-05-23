package app.cashadvisor.db.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "userAnalyticsTable")
data class UserAnalyticsEntity(
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "category")
    var categoryId: Int
)