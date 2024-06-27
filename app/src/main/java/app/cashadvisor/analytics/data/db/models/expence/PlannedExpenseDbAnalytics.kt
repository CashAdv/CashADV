package app.cashadvisor.analytics.data.db.models.expence

import app.cashadvisor.analytics.data.db.entities.AmountEntity

data class PlannedExpenseDbAnalytics(
    val listAnalytics: List<AmountEntity>
)
