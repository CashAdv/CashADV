package app.cashadvisor.analytics.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["userAnalyticsId", "categoryId"], tableName = "analyticsWithCategory",
    /*foreignKeys = [
        ForeignKey(
            entity = UserAnalyticsEntity::class,
            parentColumns = ["id"],
            childColumns = ["userAnalyticsId"]
        ),
        ForeignKey(
            entity = UserAnalyticsEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"]
        )
    ]*/
)
data class CategoryWithUserAnalyticsEntity(
    @ColumnInfo(name = "userAnalyticsId")
    val userAnalyticsId: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "planned")
    val planned: Boolean,
    @ColumnInfo(name = "categoryName")
    val categoryName: String,
    @ColumnInfo(name = "categoryIcon")
    val categoryIcon: String,
    @ColumnInfo(name = "amount")
    val amount: Int,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "date")
    val date: String
)