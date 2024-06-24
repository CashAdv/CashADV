package app.cashadvisor.analytics.data.db.models.expence

import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity

data class PlannedExpenseDbAnalytics(
    val listAnalytics: List<CategoryWithUserAnalyticsEntity>
)
