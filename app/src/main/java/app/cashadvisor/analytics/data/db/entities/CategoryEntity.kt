package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "categoryTable",
    foreignKeys = [ForeignKey(
        entity = UserAnalyticsEntity::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("id")
    )])
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "icon")
    var icon: String,
    @ColumnInfo(name = "isConstant")
    var isConstant: Boolean,
    @ColumnInfo(name = "userId")
    var userId: String
)