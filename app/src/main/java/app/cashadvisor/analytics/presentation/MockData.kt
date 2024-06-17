package app.cashadvisor.analytics.presentation

import app.cashadvisor.analytics.presentation.model.Account
import app.cashadvisor.analytics.presentation.model.AnalyticType
import app.cashadvisor.analytics.presentation.model.CategorySummary
import app.cashadvisor.analytics.presentation.model.User
import java.math.BigDecimal
import java.util.Date

object MockData {
    fun getCategorySummaryList(beginDate: Date, endDate: Date) = categorySummaryList
}


val categorySummaryList :List<CategorySummary>  = listOf (
    // Доходы факт
    CategorySummary(
        id = 3,
        name = "Зарплата",
        analyticType = AnalyticType.INCOME,
        planned = false,
        amount = BigDecimal.valueOf(75000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 4,
        name = "Наставничество",
        analyticType = AnalyticType.INCOME,
        planned = false,
        amount = BigDecimal.valueOf(20000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),

    // Расходы факт
    CategorySummary(
        id = 22,
        name = "Еда",
        analyticType = AnalyticType.EXPENSE,
        planned = false,
        amount = BigDecimal.valueOf(7563.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 18,
        name = "Развлечения",
        analyticType = AnalyticType.EXPENSE,
        planned = false,
        amount = BigDecimal.valueOf(533.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    // Фонд благосостояния факт
    CategorySummary(
        id = 25,
        name = "Инвестиции",
        analyticType = AnalyticType.SAVING,
        planned = false,
        amount = BigDecimal.valueOf(15000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 2,
        name = "Накопления",
        analyticType = AnalyticType.SAVING,
        planned = false,
        amount = BigDecimal.valueOf(30000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 24,
        name = "Валюта",
        analyticType = AnalyticType.SAVING,
        planned = false,
        amount = BigDecimal.valueOf(0.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    // Доходы план
    CategorySummary(
        id = 3,
        name = "Зарплата",
        analyticType = AnalyticType.INCOME,
        planned = true,
        amount = BigDecimal.valueOf(75000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 4,
        name = "Наставничество",
        analyticType = AnalyticType.INCOME,
        planned = true,
        amount = BigDecimal.valueOf(25000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),

    // Расходы факт
    CategorySummary(
        id = 22,
        name = "Еда",
        analyticType = AnalyticType.EXPENSE,
        planned = true,
        amount = BigDecimal.valueOf(20000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 17,
        name = "Техника",
        analyticType = AnalyticType.EXPENSE,
        planned = true,
        amount = BigDecimal.valueOf(10000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 18,
        name = "Развлечения",
        analyticType = AnalyticType.EXPENSE,
        planned = true,
        amount = BigDecimal.valueOf(10000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 12,
        name = "Транспорт",
        analyticType = AnalyticType.EXPENSE,
        planned = true,
        amount = BigDecimal.valueOf(5000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 19,
        name = "Прочее",
        analyticType = AnalyticType.EXPENSE,
        planned = true,
        amount = BigDecimal.valueOf(5000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    // Фонд благосостояния план
 /*   CategorySummary(
        id = 25,
        name = "Инвестиции",
        analyticType = AnalyticType.SAVING,
        planned = true,
        amount = BigDecimal.valueOf(15000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 2,
        name = "Накопления",
        analyticType = AnalyticType.SAVING,
        planned = true,
        amount = BigDecimal.valueOf(30000.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),
    CategorySummary(
        id = 24,
        name = "Валюта",
        analyticType = AnalyticType.SAVING,
        planned = true,
        amount = BigDecimal.valueOf(0.00),
        subcategoryList = listOf() // List<SubcategorySummary>
    ),*/
)

val account = Account(
    amount = BigDecimal.valueOf(8600000.00)
)

val user = User(
    id = 100,
    name = "Андрей",
    lastName = "Иванов"
)