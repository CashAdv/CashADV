package app.cashadvisor.authorization.presentation.viewmodel.models

sealed interface EntryScreenState {

    object Default : EntryScreenState
    object SignIn : EntryScreenState
    object SignUp : EntryScreenState

}