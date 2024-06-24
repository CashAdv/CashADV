package app.cashadvisor.analytics.data.db.models.expence

import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity

data class FactExpenseDbAnalytics(
    val listAnalytics: List<CategoryWithUserAnalyticsEntity>
)
