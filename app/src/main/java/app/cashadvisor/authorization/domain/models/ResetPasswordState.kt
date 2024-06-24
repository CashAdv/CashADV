package app.cashadvisor.authorization.domain.models

data class ResetPasswordState(
    val state: ResetPasswordState.State = ResetPasswordState.State.Initial
){
    sealed interface State {
        data object Initial : State
        data class InProcess(val codeToken: String) : State
    }
}
