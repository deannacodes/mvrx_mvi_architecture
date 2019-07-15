package com.deanna.mvrx.ui.users

import com.airbnb.mvrx.*
import com.deanna.mvrx.model.User
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
            .map(::toUsers)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(users = it)
            }
    }

    fun toUsers(users: UsersResponse): List<User> {

        return if (users.userResponses == null) emptyList()
        else users.userResponses.map {
            User(it.account_id, it.display_name, it.reputation, it.profile_image, it.website_url)
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
