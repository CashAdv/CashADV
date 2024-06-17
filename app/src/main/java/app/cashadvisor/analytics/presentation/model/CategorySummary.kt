package app.cashadvisor.analytics.presentation.model

import java.math.BigDecimal

data class CategorySummary(
    val id: Long,
    val name: String,
    val analyticType: AnalyticType,
    val planned: Boolean,
    val amount: BigDecimal,
    val completePercent: Double? = null,
    val subcategoryList: List<SubcategorySummary>?,
)
