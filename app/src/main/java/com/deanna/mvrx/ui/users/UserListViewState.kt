package com.deanna.mvrx.ui.users

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UsersResponse
import com.deanna.mvrx.mvibase.MviViewState


data class UserListViewState(
    val users: List<User>,
    val response: Async<UsersResponse>
) : MviViewState {

    companion object {
        fun idle(): UserListViewState {
            return UserListViewState(
                users = emptyList(),
                response = Uninitialized
            )
        }
    }
}