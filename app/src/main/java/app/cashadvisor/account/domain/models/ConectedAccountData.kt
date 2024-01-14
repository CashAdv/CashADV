package app.cashadvisor.account.domain.models

data class ConectedAccountData(
    val id: String,
    val userId: String,
    val bankId: String,
    val accountNumber: String,
    val accountType: String,
)
