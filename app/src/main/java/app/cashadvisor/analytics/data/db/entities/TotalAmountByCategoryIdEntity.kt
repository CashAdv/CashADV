package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "totalAmountByCategoryIdEntity")
data class TotalAmountByCategoryIdEntity(
    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    var categoryId: String,
    @ColumnInfo(name = "amount")
    var amount: Int
)