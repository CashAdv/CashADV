package app.cashadvisor.profile.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoMoreResponse(
    @SerialName("status_code") val statusCode: Int,
    val message: String,
    @SerialName("more") val userInfoMoreDto: UserInfoMoreDto,

    )

@Serializable
data class UserInfoMoreDto(
    @SerialName("app") val appDto: AppDto,
    @SerialName("settings") val settingsDto: SettingsDto
)

@Serializable
data class AppDto(
    @SerialName("category_settings") val categorySettingsDto: CategorySettingsDto,
    @SerialName("connected_accounts") val connectedAccountsDto: List<ConnectedAccountDto>
)

@Serializable
data class SettingsDto(
    @SerialName("subscriptions") val subscriptionsDto: SubscriptionsDto
)

@Serializable
data class CategorySettingsDto(
    @SerialName("expense_categories") val expenseCategoriesDto: List<ExpenseCategoryDto>,
    @SerialName("income_categories") val incomeCategoriesDto: List<IncomeCategoryDto>,
    @SerialName("investment_categories") val investmentCategoriesDto: List<InvestmentCategoryDto>
)

@Serializable
data class ConnectedAccountDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("bank_id") val bankId: String,
    @SerialName("account_number") val accountNumber: String,
    @SerialName("account_type") val accountType: String
)

@Serializable
data class ExpenseCategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    @SerialName("is_constant") val isConstant: Boolean,
    @SerialName("user_id") val userId: String
)

@Serializable
data class IncomeCategoryDto(
    val id: String,
    val icon: String,
    val name: String,
    @SerialName("is_constant") val isConstant: Boolean,
    @SerialName("user_id") val userId: String
)

@Serializable
data class InvestmentCategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    @SerialName("is_constant") val isConstant: Boolean,
    @SerialName("user_id") val userId: String
)

@Serializable
data class SubscriptionsDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    @SerialName("is_active") val isActive: Boolean,
)