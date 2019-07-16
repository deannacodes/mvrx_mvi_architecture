package com.deanna.mvrx.ui.users

import com.airbnb.mvrx.*
import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UserResponse
import com.deanna.mvrx.model.UsersResponse
import com.deanna.mvrx.mvibase.MviViewModel
import com.deanna.mvrx.mvibase.MviViewState
import com.deanna.mvrx.network.StackOverflowService
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

data class UsersState(val users: Async<List<User>> = Uninitialized) : MviViewState

class UsersViewModel @AssistedInject constructor(
    @Assisted state: UsersState,
    private val stackOverflowService: StackOverflowService
) : MviViewModel<UserListIntent, UsersState>(state) {

    init {
        fetchUsers()
    }

    override fun processIntents(intents: Observable<UserListIntent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun states(): Observable<UsersState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun fetchUsers() = withState { state ->
        if (state.users is Loading) return@withState

        stackOverflowService
            .getUsersRx()
            .map(UsersResponse::toUsers)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(users = it)
            }
    }

    fun searchUsers(query: String) = withState { state ->
        if (state.users is Loading) return@withState

        stackOverflowService
            .getUsersRxSearch(query)
            .map(UsersResponse::toUsers)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(users = it)
            }
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(state: UsersState): UsersViewModel
    }

    companion object : MvRxViewModelFactory<UsersViewModel, UsersState> {
        override fun create(viewModelContext: ViewModelContext, state: UsersState): UsersViewModel? {
            val fragment = (viewModelContext as FragmentViewModelContext).fragment<UsersFragment>()
            return fragment.viewModelFactory.create(state)
        }
    }
}
