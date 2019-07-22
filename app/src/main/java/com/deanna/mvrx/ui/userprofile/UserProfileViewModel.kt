package com.deanna.mvrx.ui.userprofile

import android.os.Parcelable
import com.airbnb.mvrx.*
import com.deanna.mvrx.model.User
import com.deanna.mvrx.mvibase.MviViewModel
import com.deanna.mvrx.repository.UserRepository
import com.deanna.mvrx.ui.users.UserListIntent
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

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
    private val userRepository: UserRepository
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
        userRepository.getUserRx(state.id).execute { copy(user = it) }
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
