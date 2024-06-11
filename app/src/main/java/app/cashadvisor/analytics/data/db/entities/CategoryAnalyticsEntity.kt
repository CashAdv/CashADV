package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["id", "categoryId"], tableName = "categoryAnalyticsTable",
    foreignKeys = [
        ForeignKey(
            entity = UserAnalyticsEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)

data class CategoryAnalyticsEntity(
    @ColumnInfo(name = "id")
    val userAnalyticsId: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "amount")
    var amount: Int,
    @ColumnInfo(name = "categoryName")
    var name: String,
    @ColumnInfo(name = "categoryIcon")
    var icon: String,
    @ColumnInfo(name = "currency")
    var currency: String
)
