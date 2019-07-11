package com.deanna.mvrx.ui.userprofile

import android.os.Parcelable
import com.airbnb.mvrx.*
import com.deanna.mvrx.MvRxViewModel
import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UsersService
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileArgs(val userId: Long) : Parcelable

data class UserProfileState(
    val userId: Long,
    val user: Async<User> = Uninitialized
) : MvRxState {
    constructor(args: UserProfileArgs) : this(userId = args.userId)
}

class UserProfileViewModel @AssistedInject constructor(
    @Assisted state: UserProfileState,
    private val usersService: UsersService
) : MvRxViewModel<UserProfileState>(state) {

    fun fetchUser() {
        withState {
            usersService
                .user(it.userId)
                .execute {
                    copy(user = it)
                }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(state: UserProfileState): UserProfileViewModel
    }

    companion object : MvRxViewModelFactory<UserProfileViewModel, UserProfileState> {
        override fun create(viewModelContext: ViewModelContext, state: UserProfileState): UserProfileViewModel? {
            val fragment = (viewModelContext as FragmentViewModelContext).fragment<UserProfileFragment>()
            return fragment.viewModelFactory.create(state)
        }
    }
}
