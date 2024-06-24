package app.cashadvisor.analytics.data.db.models.income

import app.cashadvisor.analytics.data.db.entities.CategoryWithUserAnalyticsEntity

data class FactIncomeDbAnalytics(
    val listAnalytics: List<CategoryWithUserAnalyticsEntity>
)
