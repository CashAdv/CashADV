package app.cashadvisor.analytics.presentation.model

import java.math.BigDecimal

data class Account(
    val amount: BigDecimal,
    val currency: String? = null,
)
