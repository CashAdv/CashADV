package app.cashadvisor.analytics.presentation.model

import java.math.BigDecimal

data class SubcategorySummary(
    val name: String,
    val amount: BigDecimal,
)
