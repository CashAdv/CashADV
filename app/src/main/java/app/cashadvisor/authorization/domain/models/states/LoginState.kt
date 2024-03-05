package app.cashadvisor.authorization.domain.models.states

data class LoginState(
    val state: State = State.Initial
) {
    sealed interface State {
        data object Initial : State
        data class InProcess(val codeToken: String) : State
    }
}