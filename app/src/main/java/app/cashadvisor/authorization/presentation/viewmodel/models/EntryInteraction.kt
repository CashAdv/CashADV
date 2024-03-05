package app.cashadvisor.authorization.presentation.viewmodel.models

sealed interface EntryInteraction {
    object SignUpTapped : EntryInteraction
    object SignInTapped : EntryInteraction
}