package app.cashadvisor.analytics.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserAnalyticsCategory(
    @Embedded
    val userAnalytics: UserAnalyticsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
