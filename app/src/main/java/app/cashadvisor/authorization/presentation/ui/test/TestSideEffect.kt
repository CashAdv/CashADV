package app.cashadvisor.authorization.presentation.ui.test

sealed interface TestSideEffect{
    data class ShowMessage(val message: String): TestSideEffect
}