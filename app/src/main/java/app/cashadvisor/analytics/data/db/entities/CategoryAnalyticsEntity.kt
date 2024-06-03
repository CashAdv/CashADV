package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["userAnalyticsId", "categoryId"], tableName = "categoryAnalyticsTable",
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
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "userAnalyticsId")
    val userAnalyticsId: String
)
