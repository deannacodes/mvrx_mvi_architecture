package com.deanna.mvrx.ui.users

import com.deanna.mvrx.model.User
import com.deanna.mvrx.mvibase.MviViewState

sealed class SyncState : MviViewState {
    object Idle: SyncState()

    data class Process(val type: Type): SyncState() {
        enum class Type { REFRESH }
    }

    data class Error(val error: Throwable): SyncState()
}

sealed class UserListState : MviViewState {
    data class Idle(val users: List<User>, val syncState: SyncState) : UserListState() {
        fun userClicked(userId: Int) = SelectedUser(userId)
    }

    data class SelectedUser(val userId: Int) : UserListState()

}
