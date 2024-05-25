package app.cashadvisor.profile.data.mapper

import app.cashadvisor.profile.data.dto.response.AppDto
import app.cashadvisor.profile.data.dto.response.CategorySettingsDto
import app.cashadvisor.profile.data.dto.response.ConnectedAccountDto
import app.cashadvisor.profile.data.dto.response.ExpenseCategoryDto
import app.cashadvisor.profile.data.dto.response.IncomeCategoryDto
import app.cashadvisor.profile.data.dto.response.InvestmentCategoryDto
import app.cashadvisor.profile.data.dto.response.SettingsDto
import app.cashadvisor.profile.data.dto.response.SubscriptionsDto
import app.cashadvisor.profile.data.dto.response.UserInfoMoreDto
import app.cashadvisor.profile.domain.model.App
import app.cashadvisor.profile.domain.model.CategorySettings
import app.cashadvisor.profile.domain.model.ConnectedAccount
import app.cashadvisor.profile.domain.model.ExpenseCategory
import app.cashadvisor.profile.domain.model.IncomeCategory
import app.cashadvisor.profile.domain.model.InvestmentCategory
import app.cashadvisor.profile.domain.model.Settings
import app.cashadvisor.profile.domain.model.Subscriptions
import app.cashadvisor.profile.domain.model.UserInfoMore
import javax.inject.Inject

class UserInfoMoreMapper @Inject constructor() {
    fun toUserInfoMore(userInfoMoreDto: UserInfoMoreDto) = UserInfoMore(
        appDto = toApp(userInfoMoreDto.appDto),
        settings = toSettings(userInfoMoreDto.settingsDto)
    )

    private fun toApp(appDto: AppDto) = App(
        categorySettings = toCategorySettings(appDto.categorySettingsDto),
        connectedAccounts = appDto.connectedAccountsDto.map { toConnectedAccounts(it) }
    )

    private fun toSettings(settingsDto: SettingsDto) = Settings(
        subscriptions = toSubscription(settingsDto.subscriptionsDto)
    )
    private fun toCategorySettings(categorySettingsDto: CategorySettingsDto) = CategorySettings(
        expenseCategories = categorySettingsDto.expenseCategoriesDto.map { toExpenseCategories(it) },
        incomeCategories = categorySettingsDto.incomeCategoriesDto.map { toIncomeCategories(it) },
        investmentCategories = categorySettingsDto.investmentCategoriesDto.map { toInvestmentCategories(it) }
    )
    private fun toConnectedAccounts(connectedAccountDto: ConnectedAccountDto) = ConnectedAccount(
        id = connectedAccountDto.id,
        userId = connectedAccountDto.userId,
        bankId = connectedAccountDto.bankId,
        accountNumber = connectedAccountDto.accountNumber,
        accountType = connectedAccountDto.accountType
    )
    private fun toSubscription(subscriptionsDto: SubscriptionsDto) = Subscriptions(
        id = subscriptionsDto.id,
        userId = subscriptionsDto.userId,
        startDate = subscriptionsDto.startDate,
        endDate = subscriptionsDto.endDate,
        isActive = subscriptionsDto.isActive
    )
    private fun toExpenseCategories(expenseCategoryDto: ExpenseCategoryDto) = ExpenseCategory(
        id = expenseCategoryDto.id,
        name = expenseCategoryDto.name,
        icon = expenseCategoryDto.icon,
        isConstant = expenseCategoryDto.isConstant,
        userId = expenseCategoryDto.userId
    )
    private fun toIncomeCategories(incomeCategoryDto: IncomeCategoryDto) = IncomeCategory(
        id = incomeCategoryDto.id,
        name = incomeCategoryDto.name,
        icon = incomeCategoryDto.icon,
        isConstant = incomeCategoryDto.isConstant,
        userId = incomeCategoryDto.userId
    )
    private fun toInvestmentCategories(investmentCategoryDto: InvestmentCategoryDto) = InvestmentCategory(
        id = investmentCategoryDto.id,
        name = investmentCategoryDto.name,
        icon = investmentCategoryDto.icon,
        isConstant = investmentCategoryDto.isConstant,
        userId = investmentCategoryDto.userId
    )


}
