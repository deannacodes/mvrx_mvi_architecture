package com.deanna.mvrx.ui.users

import com.deanna.mvrx.mvibase.MviIntent

sealed class UserListIntent : MviIntent {
    object InitialIntent : UserListIntent()

    object RefreshIntent : UserListIntent()

    data class SearchIntent(val searchTerm: String) : UserListIntent()
}