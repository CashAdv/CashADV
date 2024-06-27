package app.cashadvisor.analytics.data.db.models.income

import app.cashadvisor.analytics.data.db.entities.AmountEntity

data class PlannedIncomeDbAnalytics(
    val listAnalytics: List<AmountEntity>
)