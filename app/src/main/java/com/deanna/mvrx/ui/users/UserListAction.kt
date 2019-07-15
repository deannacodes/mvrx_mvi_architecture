package com.deanna.mvrx.ui.users

import com.deanna.mvrx.mvibase.MviAction

sealed class UserListAction : MviAction {
    object LoadUsersAction : UserListAction()

    data class SearchUsersAction(val searchTerm: String): UserListAction()
}