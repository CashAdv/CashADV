package app.cashadvisor.profile.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileAnalyticsResponse(
    @SerialName("status_code") val statusCode: Int,
    val message: String,
    @SerialName("response_currency") val responseCurrency: String,
    @SerialName("analytics") val userAnalyticsDto: UserAnalyticsDto
)
data class ProfileAnalyticsDto(
    val statusCode: Int,
    val message: String,
    val responseCurrency: String,
    val userAnalyticsDto: UserAnalyticsDto
)

@Serializable
data class UserAnalyticsDto(
    @SerialName("income") val incomeDto: List<IncomeDto>?,
    @SerialName("expense") val expenseDto: List<ExpenseDto>?,
    @SerialName("wealth_fund") val wealthFundDto: List<WealthFundDto>?
)

@Serializable
data class IncomeDto(
    val amount: Double,
    @SerialName("category_id") val categoryId: String,
    val date: String,
    val id: String,
    val planned: Boolean,
    @SerialName("user_id") val userId: String,
    @SerialName("bank_account") val bankAccount: String,
    val sender: String,
    val currency: String
)

@Serializable
data class ExpenseDto(
    val amount: Double,
    @SerialName("category_id") val categoryId: String,
    val date: String,
    val id: String,
    val planned: Boolean,
    @SerialName("user_id") val userId: String,
    @SerialName("bank_account") val bankAccount: String,
    @SerialName("sent_to") val sentTo: String,
    val currency: String
)

@Serializable
data class WealthFundDto(
    val amount: Double,
    val date: String,
    val id: String,
    val planned: Boolean,
    @SerialName("user_id") val userId: String,
    @SerialName("bank_account") val bankAccount: String,
    val currency: String,
    @SerialName("category_id") val categoryId: String
)