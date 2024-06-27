package app.cashadvisor.analytics.data.db.models.wealth

import app.cashadvisor.analytics.data.db.entities.AmountEntity

data class PlannedWealthDbAnalytics(
    val listAnalytics: List<AmountEntity>
)
