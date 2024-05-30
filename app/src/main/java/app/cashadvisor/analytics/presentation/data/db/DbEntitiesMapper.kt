package app.cashadvisor.analytics.presentation.data.db

import app.cashadvisor.analytics.presentation.data.db.entities.CategoryEntity
import app.cashadvisor.analytics.presentation.data.db.entities.UserAnalyticsEntity
import app.cashadvisor.profile.data.dto.response.CategorySettingsDto
import app.cashadvisor.profile.data.dto.response.ExpenseCategoryDto
import app.cashadvisor.profile.data.dto.response.ExpenseDto
import app.cashadvisor.profile.data.dto.response.IncomeCategoryDto
import app.cashadvisor.profile.data.dto.response.IncomeDto
import app.cashadvisor.profile.data.dto.response.InvestmentCategoryDto
import app.cashadvisor.profile.data.dto.response.UserAnalyticsDto
import app.cashadvisor.profile.data.dto.response.WealthFundDto


fun UserAnalyticsDto.asUserAnalyticsEntity(): List<UserAnalyticsEntity> =
    this.expenseDto.asExpenseEntityList().plus(this.incomeDto.asIncomeEntityList())
        .plus(this.wealthFundDto.asWealthEntityList())


fun List<IncomeDto>.asIncomeEntityList(): List<UserAnalyticsEntity> =
    this.map { it.asIncomeEntity() }

fun List<ExpenseDto>.asExpenseEntityList(): List<UserAnalyticsEntity> =
    this.map { it.asExpenseEntity() }

fun List<WealthFundDto>.asWealthEntityList(): List<UserAnalyticsEntity> =
    this.map { it.asWealthEntity() }

fun IncomeDto.asIncomeEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = this.id,
    amount = this.amount,
    categoryId = this.categoryId.plus(Category.INCOME.name),
    date = this.date,
    planned = this.planned,
    userId = this.userId,
    bankAccount = this.bankAccount,
    currency = this.currency,
    comment = this.sender
)

fun ExpenseDto.asExpenseEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = this.id,
    amount = this.amount,
    categoryId = this.categoryId.plus(Category.EXPENSE.name),
    date = this.date,
    planned = this.planned,
    userId = this.userId,
    bankAccount = this.bankAccount,
    currency = this.currency,
    comment = this.sentTo
)

fun WealthFundDto.asWealthEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = this.id,
    amount = this.amount,
    categoryId = this.categoryId.plus(Category.WEALTH.name),
    date = this.date,
    planned = this.planned,
    userId = this.userId,
    bankAccount = this.bankAccount,
    currency = this.currency,
    comment = " "
)

fun CategorySettingsDto.asCategoryEntity(): List<CategoryEntity> =
    this.expenseCategoriesDto.asCategoryList().plus(this.incomeCategories.asCategoryList())
        .plus(this.investmentCategoriesDto.asCategoryList())

fun List<ExpenseCategoryDto>.asCategoryList(): List<CategoryEntity> =
    this.map { it.asCategoryEntity() }

fun List<IncomeCategoryDto>.asCategoryList(): List<CategoryEntity> =
    this.map { it.asCategoryEntity() }

fun List<InvestmentCategoryDto>.asCategoryList(): List<CategoryEntity> =
    this.map { it.asCategoryEntity() }

fun ExpenseCategoryDto.asCategoryEntity(): CategoryEntity = CategoryEntity(
    id = this.id.plus(Category.EXPENSE.name),
    name = this.name,
    icon = this.icon,
    isConstant = this.isConstant,
    userId = this.userId
)

fun IncomeCategoryDto.asCategoryEntity(): CategoryEntity = CategoryEntity(
    id = this.id.plus(Category.INCOME.name),
    name = this.name,
    icon = this.icon,
    isConstant = this.isConstant,
    userId = this.userId
)

fun InvestmentCategoryDto.asCategoryEntity(): CategoryEntity = CategoryEntity(
    id = this.id.plus(Category.WEALTH.name),
    name = this.name,
    icon = this.icon,
    isConstant = this.isConstant,
    userId = this.userId
)