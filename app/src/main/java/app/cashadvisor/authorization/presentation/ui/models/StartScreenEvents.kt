package app.cashadvisor.authorization.presentation.ui.models

sealed interface StartScreenEvents {
    data object ProceedNextClicked : StartScreenEvents
    data object UiKitClicked : StartScreenEvents
}