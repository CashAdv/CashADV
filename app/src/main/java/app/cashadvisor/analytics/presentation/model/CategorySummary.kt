package app.cashadvisor.analytics.presentation.model

import java.math.BigDecimal

data class CategorySummary(
    val id: Long,
    val name: String,
    val amount: BigDecimal,
    val subcategoryList: List<SubcategorySummary>,
)
