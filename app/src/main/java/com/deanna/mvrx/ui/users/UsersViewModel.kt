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
import io.reactivex.subjects.BehaviorSubject

/* MvRx has a class, Async. Under the hood it's an observable
 * It's a sealed class with Uninitialized, Loading, Success, and Fail types
 * we can check the type and render accordingly */
data class UsersState(val users: Async<List<User>> = Uninitialized,
                      val query: String = "") : MvRxState

class UsersViewModel @AssistedInject constructor(
    @Assisted state: UsersState,
    private val stackOverflowService: StackOverflowService
) : MviViewModel<UserListIntent, UsersState>(state) {

    private val intentsSubject: BehaviorSubject<UserListIntent> = BehaviorSubject.create()
    private val disposables = CompositeDisposable()

    override fun processIntents(intents: Observable<UserListIntent>) {
        /* subscribe to our intents with a BehaviorSubject
         * locally subscribe to to the BehaviorSubject */
        disposables.add(intents.subscribe(intentsSubject::onNext))
        disposables.add(intentsSubject.subscribe { actionFromIntent(it) })
    }

    /* we can do actions directly from the intent since the
     * ViewModel directly accesses, modifies, and renders state */
    private fun actionFromIntent(intent: UserListIntent) {
        return when (intent) {
            UserListIntent.InitialIntent -> fetchInitialUsers()
            UserListIntent.RefreshIntent -> fetchUsers()
            UserListIntent.ClearSearchIntent -> fetchUsers()
            is UserListIntent.SearchIntent -> searchUsers(intent.searchTerm)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    /*  With these functions, we're returning an observable, then using
     *  execute to subscribe to it and emit state (Loading, Success, etc).
     *  Loading will actually have a "null" value, whereas Success will
     *  include the result.
     *  the subscription disposal is taken care of by MvRx */
    private fun fetchInitialUsers() = withState { state ->
        if (state.users is Loading) return@withState

        if (state.users is Uninitialized) {
            stackOverflowService.getUsersRx()
                .map(UsersResponse::toUsers)
                .subscribeOn(Schedulers.io())
                .execute { copy(users = it) }
        } else {
            return@withState
        }
    }

    private fun fetchUsers() = withState { state ->
        if (state.users is Loading) return@withState

        stackOverflowService.getUsersRx()
            .map(UsersResponse::toUsers)
            .subscribeOn(Schedulers.io())
            .execute { copy(users = it) }
    }

    private fun searchUsers(query: String) = withState { state ->
        if (query != state.query){
            if (state.users is Loading) return@withState

            stackOverflowService.getUsersRxSearch(query)
                .map(UsersResponse::toUsers)
                .subscribeOn(Schedulers.io())
                .execute { copy(users = it, query = query) }}
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
