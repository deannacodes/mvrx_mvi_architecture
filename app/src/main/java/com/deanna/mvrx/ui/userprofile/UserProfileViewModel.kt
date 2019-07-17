package com.deanna.mvrx.ui.userprofile

import android.os.Parcelable
import android.util.Log
import com.airbnb.mvrx.*
import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UserResponse
import com.deanna.mvrx.model.UsersResponse
import com.deanna.mvrx.mvibase.MviViewModel
import com.deanna.mvrx.network.StackOverflowService
import com.deanna.mvrx.ui.users.UserListIntent
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import java.util.logging.Logger

@Parcelize
data class UserProfileArgs(val id: Int) : Parcelable

data class UserProfileState(
    val id: Int,
    val user: Async<User> = Uninitialized
) : MvRxState {
    constructor(args: UserProfileArgs) : this(id = args.id)
}

class UserProfileViewModel @AssistedInject constructor(
    @Assisted state: UserProfileState,
    private val stackOverflowService: StackOverflowService
) : MviViewModel<UserListIntent, UserProfileState>(state) {

    init {
        fetchUser()
    }

    override fun processIntents(intents: Observable<UserListIntent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun states(): Observable<UserProfileState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun fetchUser() = withState { state ->
        if (state.user is Loading) return@withState

        stackOverflowService
            .getUserDetail(state.id.toString())
            .doOnSuccess { Log.w("Response: ", it.toString()) }
            .map(UsersResponse::toUser)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(user = it)
            }
    }

    private fun toUser(user: UserResponse): User {
        return User(
            user.user_id ?: -1,
            user.display_name ?: "",
            user.reputation ?: -1,
            user.profile_image ?: "",
            user.website_url ?: ""
        )
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
