package com.deanna.mvrx.ui.users

import com.deanna.mvrx.model.User
import com.deanna.mvrx.mvibase.MviResult

sealed class UserListResult: MviResult {
    sealed class LoadUsersResult : UserListResult() {
        data class Success(val users: List<User>) : LoadUsersResult()
        data class Failure(val error: Throwable) : LoadUsersResult()
        object InFlight : LoadUsersResult()
    }

    data class SearchUsersResult(val users: List<User>): UserListResult()
}