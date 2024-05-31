package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categoryAnalyticsTable",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ),
        ForeignKey(
            entity = UserAnalyticsEntity::class,
            parentColumns = ["id"],
            childColumns = ["userAnalyticsId"]
        )
    ]
)
data class CategoryAnalyticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "userAnalyticsId")
    val userAnalyticsId: String,
    @ColumnInfo(name = "categoryName")
    var name: String,
    @ColumnInfo(name = "categoryIcon")
    var icon: String,
    @ColumnInfo(name = "userAnalyticsDate")
    var date: String,
    @ColumnInfo(name = "userAnalyticsAmount")
    var amount: Int
)
