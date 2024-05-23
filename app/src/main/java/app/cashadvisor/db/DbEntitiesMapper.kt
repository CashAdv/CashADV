package app.cashadvisor.db

import app.cashadvisor.db.Entities.UserAnalyticsEntity
import app.cashadvisor.profile.data.dto.response.ExpenseDto
import app.cashadvisor.profile.data.dto.response.IncomeDto
import app.cashadvisor.profile.data.dto.response.UserAnalyticsDto
import app.cashadvisor.profile.data.dto.response.WealthFundDto


fun UserAnalyticsDto.asEntity(): List<UserAnalyticsEntity> =
    this.expenseDto.asEntity().plus(this.incomeDto.asEntity()).plus(this.wealthFundDto.asEntity())


fun List<IncomeDto>.asEntity(): List<UserAnalyticsEntity> = this.map { it.asEntity() }

fun List<ExpenseDto>.asEntity(): List<UserAnalyticsEntity> = this.map { it.asEntity() }

fun List<WealthFundDto>.asEntity(): List<UserAnalyticsEntity> = this.map { it.asEntity() }

fun IncomeDto.asEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    date = this.date,
    categoryId = this.categoryId.toInt()
)

fun ExpenseDto.asEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    date = this.date,
    categoryId = this.categoryId.toInt()
)

fun WealthFundDto.asEntity(): UserAnalyticsEntity = UserAnalyticsEntity(
    date = this.date,
    categoryId = this.categoryId.toInt()
)