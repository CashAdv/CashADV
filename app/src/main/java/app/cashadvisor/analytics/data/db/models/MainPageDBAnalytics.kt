package app.cashadvisor.analytics.data.db.models

import app.cashadvisor.analytics.data.db.models.expence.ExpenseDbAnalytics
import app.cashadvisor.analytics.data.db.models.income.IncomeDbAnalytics
import app.cashadvisor.analytics.data.db.models.wealth.WealthDbAnalytics

data class MainPageDBAnalytics(
    val dateStart: String,
    val dateEnd: String,
    val incomeDbAnalytics: IncomeDbAnalytics,
    val expenseDbAnalytics: ExpenseDbAnalytics,
    val wealthDbAnalytics: WealthDbAnalytics
)
