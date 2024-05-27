package app.cashadvisor.db

import app.cashadvisor.db.entities.UserAnalyticsEntity
import app.cashadvisor.profile.data.dto.response.ExpenseDto
import app.cashadvisor.profile.data.dto.response.IncomeDto
import app.cashadvisor.profile.data.dto.response.UserAnalyticsDto
import app.cashadvisor.profile.data.dto.response.WealthFundDto


fun UserAnalyticsDto.asUserAnalyticsEntity(): List<UserAnalyticsEntity> =
    this.expenseDto.asExpenseEntityList().plus(this.incomeDto.asIncomeEntityList()).plus(this.wealthFundDto.asWealthEntityList())


fun List<IncomeDto>.asIncomeEntityList(): List<UserAnalyticsEntity> = this.map { it.asIncomeEntity() }

fun List<ExpenseDto>.asExpenseEntityList(): List<UserAnalyticsEntity> = this.map { it.asExpenseEntity() }

fun List<WealthFundDto>.asWealthEntityList(): List<UserAnalyticsEntity> = this.map { it.asWealthEntity() }

fun IncomeDto.asIncomeEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = null,
    userId = this.userId,
    date = this.date,
    amount = this.amount,
    categoryId = this.categoryId.toInt(),
    planned = this.planned,
    currency = this.currency,
    bankAccount = this.bankAccount
)

fun ExpenseDto.asExpenseEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = null,
    userId = this.userId,
    date = this.date,
    amount = this.amount,
    categoryId = this.categoryId.toInt(),
    planned = this.planned,
    currency = this.currency,
    bankAccount = this.bankAccount
)

fun WealthFundDto.asWealthEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    id = null,
    userId = this.userId,
    date = this.date,
    amount = this.amount,
    categoryId = this.categoryId.toInt(),
    planned = this.planned,
    currency = this.currency,
    bankAccount = this.bankAccount
)