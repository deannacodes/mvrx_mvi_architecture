package com.deanna.mvrx.ui.users

import com.airbnb.mvrx.*
import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UsersResponse
import com.deanna.mvrx.mvibase.MviViewModel
import com.deanna.mvrx.network.StackOverflowService
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

data class UsersState(val users: Async<List<User>> = Uninitialized) : MvRxState

class UsersViewModel @AssistedInject constructor(
    @Assisted state: UsersState,
    private val stackOverflowService: StackOverflowService
) : MviViewModel<UserListIntent, UsersState>(state) {

    private val intentsSubject: PublishSubject<UserListIntent> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    private val intentsObservable = intentsSubject.hide()

    init {
        fetchUsers()
    }

    override fun processIntents(intents: Observable<UserListIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    private fun actionFromIntent(intent: UserListIntent) {
        return when (intent) {
            UserListIntent.InitialIntent -> fetchUsers()
            UserListIntent.RefreshIntent -> fetchUsers()
            UserListIntent.ClearSearchIntent -> fetchUsers()
            is UserListIntent.SearchIntent -> searchUsers(intent.searchTerm)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


    private fun fetchUsers() = withState { state ->

        stackOverflowService
            .getUsersRx()
            .map(UsersResponse::toUsers)
            .subscribeOn(Schedulers.io())
            .execute {
                copy(users = it)
            }
    }

    private fun searchUsers(query: String) = withState { state ->

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
