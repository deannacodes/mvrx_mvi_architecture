package com.deanna.mvrx.ui.users

sealed class UserListViewEvent {
    data class UserClick(val userId: Int) : UserListViewEvent()
    object RefreshUsersSwipe : UserListViewEvent()
    data class FilterUserList(val searchTerm: String) : UserListViewEvent()
}