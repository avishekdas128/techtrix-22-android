package com.orangeink.common

import com.google.firebase.auth.FirebaseUser

interface IEventHandler {
    fun handleEvent(event: UIEvent)
}

sealed class UIEvent {
    object ShowForceUpdateDialog : UIEvent()
    object OpenLoginBottomSheet : UIEvent()
    object ProfileSetupCompleted : UIEvent()
    object ProfileEditCompleted : UIEvent()
    data class SearchQueryUpdate(val query: String) : UIEvent()
    data class OpenProfileSetupBottomSheet(val user: FirebaseUser) : UIEvent()
}

