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
            ExpenseCategoryDto("2", "Кредит", "icon_2", false, "1"),
            ExpenseCategoryDto("3", "Развлечения", "icon_3", true, "1"),
            ExpenseCategoryDto("4", "Транспорт", "icon_1", true, "1"),
            ExpenseCategoryDto("5", "Долги", "icon_2", false, "1")
        ),
        listOf(
            IncomeCategoryDto("1", "icon_1", "Зарплата", true, "1"),
            IncomeCategoryDto("2", "icon_2", "Подработка", false, "1"),
            IncomeCategoryDto("3", "icon_3", "Дивиденды", false, "1"),
        ),
        listOf(
            InvestmentCategoryDto("1", "Акции", "icon_1", true, "1"),
            InvestmentCategoryDto("2", "Облигации", "icon_2", false, "1"),
            InvestmentCategoryDto("3", "Крипта", "icon_3", true, "1"),
        )
    )
)

data class TestUserAnalyticsResponse(
    val userAnalyticsDto: UserAnalyticsDto = UserAnalyticsDto(
        listOf(
            IncomeDto(100, "1", "12.11.2010", "1", true, "1", "1", "Зарплата", "1"),
            IncomeDto(200, "1", "11.12.2011", "2", true, "1", "1", "Зарплата", "1"),
            IncomeDto(300, "1", "10.10.2012", "3", false, "1", "1", "Зарплата", "1"),
            IncomeDto(150, "1", "13.11.2010", "4", true, "2", "2", "Зарплата", "1"),
            IncomeDto(250, "2", "14.12.2011", "5", false, "2", "2", "Подработка", "1"),
            IncomeDto(350, "3", "15.10.2012", "6", true, "2", "2", "Подработка", "1"),
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
            ExpenseDto(250, "2", "14.12.2011", "5", false, "2", "1", "Машина", "1"),
            ExpenseDto(250, "2", "14.12.2011", "6", false, "2", "1", "Квартира", "1"),
            ExpenseDto(250, "2", "01.05.2024", "7", false, "2", "1", "Машина", "1"),
            ExpenseDto(250, "2", "15.05.2024", "8", false, "2", "1", "Квартира", "1"),
            ExpenseDto(250, "2", "16.05.2024", "9", false, "2", "1", "Машина", "1"),
            ExpenseDto(350, "3", "31.05.2024", "10", false, "2", "1", "Кино", "1"),
            ExpenseDto(250, "2", "01.05.2024", "11", true, "2", "1", "Квартира", "1"),
            ExpenseDto(250, "1", "14.05.2024", "12", true, "2", "1", "Фастфуд", "1"),
            ExpenseDto(250, "1", "15.05.2024", "13", true, "2", "1", "Магазин", "1"),
            ExpenseDto(350, "1", "31.05.2024", "14", true, "2", "1", "Ресторан", "1")
        ),
        listOf(
            WealthFundDto(100, "12.11.2010", "1", true, "1", "1", "1", "1"),
            WealthFundDto(200, "11.12.2011", "2", true, "1", "1", "1", "2"),
            WealthFundDto(300, "10.10.2012", "3", false, "1", "1", "1", "3"),
            WealthFundDto(150, "13.11.2010", "4", true, "2", "2", "1", "1"),
            WealthFundDto(250, "14.12.2011", "5", false, "2", "2", "1", "2"),
            WealthFundDto(350, "15.10.2012", "6", true, "2", "2", "1", "3"),
            WealthFundDto(250, "01.05.2024", "7", false, "2", "2", "1", "1"),
            WealthFundDto(250, "16.05.2024", "8", false, "2", "2", "1", "2"),
            WealthFundDto(250, "15.05.2024", "9", false, "2", "2", "1", "3"),
            WealthFundDto(250, "31.05.2024", "10", false, "2", "2", "1", "1"),
            WealthFundDto(250, "01.05.2024", "11", true, "2", "2", "1", "2"),
            WealthFundDto(250, "15.05.2024", "12", true, "2", "2", "1", "3"),
            WealthFundDto(250, "16.05.2024", "13", true, "2", "2", "1", "1"),
            WealthFundDto(250, "31.05.2024", "14", true, "2", "2", "1", "2")
        )
    )
)