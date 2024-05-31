package app.cashadvisor.analytics.data.db

import androidx.room.Embedded
import androidx.room.Relation
import app.cashadvisor.analytics.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.data.db.entities.UserAnalyticsEntity

data class CategoryWithUserAnalytics(
    @Embedded
    var category: CategoryEntity,
    @Relation(parentColumn = "id", entityColumn = "categoryId")
    var userAnalytics: List<UserAnalyticsEntity>
)