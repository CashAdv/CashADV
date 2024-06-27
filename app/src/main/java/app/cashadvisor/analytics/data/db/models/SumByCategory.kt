package app.cashadvisor.analytics.data.db.models

data class SumByCategory(
    val categoryId: String,
    val title: String,
    val sum: Int,
    val categoryName: String,
    val categoryIcon: String
)
