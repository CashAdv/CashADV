package app.cashadvisor.analytics.presentation.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.cashadvisor.analytics.presentation.data.db.Category


@Entity(tableName = "categoryTable")
data class CategoryEntity(
    @PrimaryKey
    var id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "icon")
    var icon: String,
    @ColumnInfo(name = "isConstant")
    var isConstant: Boolean,
    @ColumnInfo(name = "userId")
    var userId: String,
    @ColumnInfo(name = "category")
    var category: Category
)