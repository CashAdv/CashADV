package app.cashadvisor.profile.domain.model

data class ProfileAnalytics(
    val statusCode: Int,
    val message: String,
    val responseCurrency: String,
    val userAnalytics: UserAnalytics
)

data class UserAnalytics(
    val income: List<Income>,
    val expense: List<Expense>,
    val wealthFund: List<WealthFund>
)

data class Income(
    val amount: Int,
    val categoryId: String,
    val date: String,
    val id: String,
    val planned: Boolean,
    val userId: String,
    val bankAccount: String,
    val sender: String,
    val currency: String
)

data class Expense(
    val amount: Int,
    val categoryId: String,
    val date: String,
    val id: String,
    val planned: Boolean,
    val userId: String,
    val bankAccount: String,
    val sentTo: String,
    val currency: String
)

data class WealthFund(
    val amount: Int,
    val date: String,
    val id: String,
    val planned: Boolean,
    val userId: String,
    val bankAccount: String,
    val currency: String,
    val categoryId: String
)

