package app.cashadvisor.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categoryTable")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    @ColumnInfo(name = "category")
    var category: String
)