package app.cashadvisor.analytics.data.db.models.wealth

import app.cashadvisor.analytics.data.db.entities.AmountEntity

data class FactWealthDbAnalytics(
    val listAnalytics: List<AmountEntity>
)
