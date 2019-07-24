package com.deanna.mvrx.ui.userprofile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.deanna.mvrx.mvibase.BaseFragment
import com.deanna.mvrx.mvibase.simpleController
import com.deanna.mvrx.ui.views.profile
import javax.inject.Inject

class UserProfileFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: UserProfileViewModel.Factory
    private val viewModel: UserProfileViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asyncSubscribe(UserProfileState::user)
        searchView.isVisible = false
        swipeRefreshLayout.setOnRefreshListener { viewModel.fetchUser() }
    }

    override fun invalidate() = withState(viewModel) { state ->
        super.invalidate()
        swipeRefreshLayout.isRefreshing = state.user is Loading || state.user is Uninitialized
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val u = state.user.invoke()

        if (u == null) {
            swipeRefreshLayout.isRefreshing = true
        } else {
            swipeRefreshLayout.isRefreshing = false
            profile {
                id(u.userId.toString())
                profileName(u.userName)
                userId(u.userId.toString())
                rep("Rep: " + u.reputation.toString())
                website(u.websiteUrl)
                image(u.imageUrl)

            }
        }
    }
}
