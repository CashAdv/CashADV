package app.cashadvisor.authorization.presentation.ui.test

sealed interface TestScreenEvent {
    class SetEmail(val email: String) : TestScreenEvent
    data object Register : TestScreenEvent
    data object Login : TestScreenEvent
    class SetRegisterConformationCode(val code: String) : TestScreenEvent
    class SetLoginConformationCode(val code: String) : TestScreenEvent
    data object ConfirmRegister : TestScreenEvent
    data object ConfirmLogin : TestScreenEvent
}