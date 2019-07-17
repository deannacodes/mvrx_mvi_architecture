package com.deanna.mvrx.ui.users

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import com.airbnb.mvrx.*
import com.deanna.mvrx.R
import com.deanna.mvrx.mvibase.BaseFragment
import com.deanna.mvrx.mvibase.simpleController
import com.deanna.mvrx.ui.userprofile.UserProfileArgs
import com.deanna.mvrx.ui.views.basicRow
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: UsersViewModel.Factory
    private val viewModel: UsersViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /* subscribe to Async object in the state of the viewModel
         * we'll know when it changes */
        viewModel.asyncSubscribe(UsersState::users) {
            //do something on success
        }
        /* send intents directly to the view model
        *  keep the fragment dumb */
        viewModel.processIntents(intents())
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val list = state.users.invoke()
        list?.forEach { user ->
            basicRow {
                id("ID: " + user.userId.toString())
                title(user.userName)
                subtitle("Rep: " + user.reputation.toString())
                image(user.imageUrl)
                clickListener { _ ->
                    navigateTo(
                        R.id.action_usersListIndex_to_usersProfileFragment,
                        UserProfileArgs(user.userId)
                    )
                }
            }
        }
    }

    /* this gets called anytime the viewModel's state changes
    *  epoxy by design will do a diff check on data
    *  you can access the state of multiple viewModels as well */
    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        swipeRefreshLayout.isRefreshing = state.users is Loading || state.users is Uninitialized
    }

    /* send all intents to the viewModel via this observable */
    private fun intents(): Observable<UserListIntent> {
        return Observable.merge(
            initialIntent(),
            refreshIntent(),
            clearSearchIntent(),
            searchIntent()
        )
    }

    private fun initialIntent(): Observable<UserListIntent.InitialIntent> {
        return Observable.just(UserListIntent.InitialIntent)
    }

    private fun refreshIntent(): Observable<UserListIntent.RefreshIntent> {
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
            .map { UserListIntent.RefreshIntent }
    }

    private fun clearSearchIntent(): Observable<UserListIntent.ClearSearchIntent> {
        val searchViewRelay = PublishRelay.create<UserListIntent.ClearSearchIntent>()
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                searchViewRelay.accept(UserListIntent.ClearSearchIntent)
                return false
            }
        })
        return searchViewRelay.hide()
    }

    private fun searchIntent(): Observable<UserListIntent.SearchIntent> {
        return RxSearchView.queryTextChanges(searchView).map {
            UserListIntent.SearchIntent(it.toString())
        }.debounce(300, TimeUnit.MILLISECONDS)
    }

}

fun Any.logError(throwable: Throwable, message: String = "") = Log.e(javaClass.simpleName, message, throwable)