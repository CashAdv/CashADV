package app.cashadvisor.analytics.data.db.models.wealth

import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity

data class FactWealthDbAnalytics(
    val listAnalytics: List<CategoryWithUserAnalyticsEntity>
)
