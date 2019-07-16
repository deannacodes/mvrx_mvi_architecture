package com.deanna.mvrx.ui.users

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.deanna.mvrx.R
import com.deanna.mvrx.mvibase.BaseFragment
import com.deanna.mvrx.mvibase.simpleController
import com.deanna.mvrx.ui.userprofile.UserProfileArgs
import com.deanna.mvrx.ui.views.basicRow
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UsersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: UsersViewModel.Factory
    private val viewModel: UsersViewModel by fragmentViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asyncSubscribe(UsersState::users)
        swipeRefreshLayout.setOnRefreshListener { viewModel.fetchUsers() }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUsers(query ?: "")
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean = false
        })
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.fetchUsers()
                return false
            }
        })
    }


    override fun epoxyController() = simpleController(viewModel) { state ->

        val list = state.users.invoke()

        list?.forEach { user ->
            basicRow {
                id(user.userId.toString())
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


    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        swipeRefreshLayout.isRefreshing = state.users is Loading || state.users is Uninitialized
    }

}


fun Any.logError(throwable: Throwable, message: String = "") = Log.e(javaClass.simpleName, message, throwable)