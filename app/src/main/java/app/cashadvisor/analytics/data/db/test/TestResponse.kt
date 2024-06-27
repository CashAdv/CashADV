package app.cashadvisor.analytics.data.db.test

import app.cashadvisor.profile.data.dto.response.CategorySettingsDto
import app.cashadvisor.profile.data.dto.response.ExpenseCategoryDto
import app.cashadvisor.profile.data.dto.response.ExpenseDto
import app.cashadvisor.profile.data.dto.response.IncomeCategoryDto
import app.cashadvisor.profile.data.dto.response.IncomeDto
import app.cashadvisor.profile.data.dto.response.InvestmentCategoryDto
import app.cashadvisor.profile.data.dto.response.UserAnalyticsDto
import app.cashadvisor.profile.data.dto.response.WealthFundDto

data class TestCategoryResponse(
    val categorySettingsDto: CategorySettingsDto = CategorySettingsDto(
        listOf(
            ExpenseCategoryDto("1", "Еда", "icon_1", true, "1"),
            ExpenseCategoryDto("2", "Petr", "icon_2", false, "1"),
            ExpenseCategoryDto("3", "Slava", "icon_3", true, "1"),
            ExpenseCategoryDto("4", "Ivan", "icon_1", true, "2"),
            ExpenseCategoryDto("5", "Petr", "icon_2", false, "2"),
            ExpenseCategoryDto("6", "Slava", "icon_3", true, "2"),
            ExpenseCategoryDto("7", "Кредит", "icon_2", false, "2"),
            ExpenseCategoryDto("8", "Еда", "icon_2", false, "2"),
            ExpenseCategoryDto("9", "Развлечения", "icon_2", false, "2"),
            ExpenseCategoryDto("10", "Транспорт", "icon_2", false, "2"),
            ExpenseCategoryDto("11", "Кредит", "icon_2", true, "2"),
            ExpenseCategoryDto("12", "Еда", "icon_2", true, "2"),
            ExpenseCategoryDto("13", "Развлечения", "icon_2", true, "2"),
            ExpenseCategoryDto("14", "Транспорт", "icon_2", true, "2")
        ),
        listOf(
            IncomeCategoryDto("1", "icon_1", "Ivan", true, "1"),
            IncomeCategoryDto("2", "icon_2", "Petr", false, "1"),
            IncomeCategoryDto("3", "icon_3", "Slava", true, "1"),
            IncomeCategoryDto("4", "icon_1", "Ivan", true, "2"),
            IncomeCategoryDto("5", "icon_2", "Petr", false, "2"),
            IncomeCategoryDto("6", "icon_3", "Slava", true, "2")
        ),
        listOf(
            InvestmentCategoryDto("1", "Ivan", "icon_1", true, "1"),
            InvestmentCategoryDto("2", "Petr", "icon_2", false, "1"),
            InvestmentCategoryDto("3", "Slava", "icon_3", true, "1"),
            InvestmentCategoryDto("4", "Ivan", "icon_1", true, "2"),
            InvestmentCategoryDto("5", "Petr", "icon_2", false, "2"),
            InvestmentCategoryDto("6", "Slava", "icon_3", true, "2")
        )
    )
)

data class TestUserAnalyticsResponse(
    val userAnalyticsDto: UserAnalyticsDto = UserAnalyticsDto(
        listOf(
            IncomeDto(100, "1", "12.11.2010", "1", true, "1", "1", "Bank", "1"),
            IncomeDto(200, "2", "11.12.2011", "2", true, "1", "1", "Mama", "1"),
            IncomeDto(300, "3", "10.10.2012", "3", false, "1", "1", "Salary", "1"),
            IncomeDto(150, "1", "13.11.2010", "4", true, "2", "2", "Petr", "1"),
            IncomeDto(250, "2", "14.12.2011", "5", false, "2", "2", "Salary", "1"),
            IncomeDto(350, "3", "15.10.2012", "6", true, "2", "2", "Salary", "1"),
            IncomeDto(250, "2", "01.05.2024", "7", false, "2", "2", "Зарплата", "1"),
            IncomeDto(250, "2", "15.05.2024", "8", false, "2", "2", "Репетиторство", "1"),
            IncomeDto(250, "2", "16.05.2024", "9", false, "2", "2", "Вклад", "1"),
            IncomeDto(250, "2", "31.05.2024", "10", false, "2", "2", "Подработка", "1"),
            IncomeDto(250, "2", "01.05.2024", "11", true, "2", "2", "Зарплата", "1"),
            IncomeDto(250, "2", "15.05.2024", "12", true, "2", "2", "Репетиторство", "1"),
            IncomeDto(250, "2", "16.05.2024", "13", true, "2", "2", "Вклад", "1"),
            IncomeDto(250, "2", "31.05.2024", "14", true, "2", "2", "Подработка", "1")
        ),
        listOf(
            ExpenseDto(100, "1", "12.11.2010", "1", true, "1", "2", "Ресторан", "1"),
            ExpenseDto(200, "2", "11.12.2011", "2", true, "1", "2", "Ресторан", "1"),
            ExpenseDto(300, "3", "10.10.2012", "3", false, "1", "2", "Продукты", "1"),
            ExpenseDto(150, "1", "13.11.2010", "4", true, "2", "1", "Продукты", "1"),
            ExpenseDto(250, "2", "14.12.2011", "5", false, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "2", "14.12.2011", "6", false, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "2", "01.05.2024", "7", false, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "2", "15.05.2024", "8", false, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "2", "16.05.2024", "9", false, "2", "1", "Ivan", "1"),
            ExpenseDto(350, "3", "31.05.2024", "10", false, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "2", "01.05.2024", "11", true, "2", "1", "Ivan", "1"),
            ExpenseDto(250, "1", "14.05.2024", "12", true, "2", "1", "Фастфуд", "1"),
            ExpenseDto(250, "1", "15.05.2024", "13", true, "2", "1", "Магазин", "1"),
            ExpenseDto(350, "1", "31.05.2024", "14", true, "2", "1", "Ресторан", "1")
        ),
        listOf(
            WealthFundDto(100, "12.11.2010", "1", true, "1", "1", "1", "1"),
            WealthFundDto(200, "11.12.2011", "2", true, "1", "1", "1", "2"),
            WealthFundDto(300, "10.10.2012", "3", false, "1", "1", "1", "3"),
            WealthFundDto(150, "13.11.2010", "4", true, "2", "2", "1", "4"),
            WealthFundDto(250, "14.12.2011", "5", false, "2", "2", "1", "5"),
            WealthFundDto(350, "15.10.2012", "6", true, "2", "2", "1", "6"),
            WealthFundDto(250, "01.05.2024", "7", false, "2", "2", "1", "5"),
            WealthFundDto(250, "16.05.2024", "8", false, "2", "2", "1", "5"),
            WealthFundDto(250, "15.05.2024", "9", false, "2", "2", "1", "5"),
            WealthFundDto(250, "31.05.2024", "10", false, "2", "2", "1", "5"),
            WealthFundDto(250, "01.05.2024", "11", true, "2", "2", "1", "5"),
            WealthFundDto(250, "15.05.2024", "12", true, "2", "2", "1", "5"),
            WealthFundDto(250, "16.05.2024", "13", true, "2", "2", "1", "5"),
            WealthFundDto(250, "31.05.2024", "14", true, "2", "2", "1", "5")
        )
    )
)