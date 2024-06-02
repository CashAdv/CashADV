package app.cashadvisor.profile.domain.model


data class UserInfoMore(
    val appDto: App,
    val settings: Settings
)

data class App(
    val categorySettings: CategorySettings,
    val connectedAccounts: List<ConnectedAccount>?
)

data class Settings(
    val subscriptions: Subscriptions
)

data class CategorySettings(
    val expenseCategories: List<ExpenseCategory>?,
    val incomeCategories: List<IncomeCategory>?,
    val investmentCategories: List<InvestmentCategory>?
)

data class ConnectedAccount(
    val id: String,
    val userId: String,
    val bankId: String,
    val accountNumber: String,
    val accountType: String
)
data class ExpenseCategory(
    val id: String,
    val name: String,
    val icon: String,
    val isConstant: Boolean,
    val userId: String
)
data class IncomeCategory(
    val id: String,
    val icon: String,
    val name: String,
    val isConstant: Boolean,
    val userId: String
)
data class InvestmentCategory(
    val id: String,
    val name: String,
    val icon: String,
    val isConstant: Boolean,
    val userId: String
)
data class Subscriptions(
    val id: String,
    val userId: String,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean,
)


