package app.cashadvisor.profile.domain.mapper

import app.cashadvisor.profile.data.dto.response.ExpenseDto
import app.cashadvisor.profile.data.dto.response.IncomeDto
import app.cashadvisor.profile.data.dto.response.ProfileAnalyticsDto
import app.cashadvisor.profile.data.dto.response.UserAnalyticsDto
import app.cashadvisor.profile.data.dto.response.WealthFundDto
import app.cashadvisor.profile.domain.model.Expense
import app.cashadvisor.profile.domain.model.Income
import app.cashadvisor.profile.domain.model.ProfileAnalytics
import app.cashadvisor.profile.domain.model.UserAnalytics
import app.cashadvisor.profile.domain.model.WealthFund
import javax.inject.Inject

class ProfileAnalyticsDomainMapper @Inject constructor(){
    fun toProfileAnalytics(profileAnalyticsDto: ProfileAnalyticsDto) = ProfileAnalytics(
        statusCode = profileAnalyticsDto.statusCode,
        message = profileAnalyticsDto.message,
        responseCurrency = profileAnalyticsDto.responseCurrency,
        userAnalytics = toUserAnalytics(profileAnalyticsDto.userAnalyticsDto)
    )
    private fun toUserAnalytics(userAnalyticsDto: UserAnalyticsDto) = UserAnalytics(
        income = userAnalyticsDto.incomeDto.map { toIncome(it) },
        expense = userAnalyticsDto.expenseDto.map { toExpense(it) },
        wealthFund = userAnalyticsDto.wealthFundDto.map { toWealthFund(it) }
    )
    private fun toIncome(incomeDto: IncomeDto) = Income(
        amount = incomeDto.amount,
        categoryId = incomeDto.categoryId,
        date = incomeDto.date,
        id = incomeDto.id,
        planned = incomeDto.planned,
        userId = incomeDto.userId,
        bankAccount = incomeDto.bankAccount,
        sender = incomeDto.sender,
        currency = incomeDto.currency
    )
    private fun toExpense(expenseDto: ExpenseDto) = Expense(
        amount = expenseDto.amount,
        categoryId = expenseDto.categoryId,
        date = expenseDto.date,
        id = expenseDto.id,
        planned = expenseDto.planned,
        userId = expenseDto.userId,
        bankAccount = expenseDto.bankAccount,
        sentTo = expenseDto.sentTo,
        currency = expenseDto.currency
    )
    private fun toWealthFund(wealthFundDto: WealthFundDto) = WealthFund(
        amount = wealthFundDto.amount,
        categoryId = wealthFundDto.categoryId,
        date = wealthFundDto.date,
        id = wealthFundDto.id,
        planned = wealthFundDto.planned,
        userId = wealthFundDto.userId,
        bankAccount = wealthFundDto.bankAccount,
        currency = wealthFundDto.currency
    )
}