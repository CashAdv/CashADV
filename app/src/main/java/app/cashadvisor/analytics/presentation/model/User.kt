package app.cashadvisor.analytics.presentation.model

data class User(
    val id: Long,
    val name: String,
    val lastName: String,
    val avatarUrl: String? = null,
)
