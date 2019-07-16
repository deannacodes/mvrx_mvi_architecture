package com.deanna.mvrx.ui.userprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.*
import com.deanna.mvrx.R
import com.deanna.mvrx.mvibase.BaseFragment
import com.deanna.mvrx.mvibase.simpleController
import com.deanna.mvrx.ui.views.profile
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UserProfileFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: UserProfileViewModel.Factory
    private val viewModel: UserProfileViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asyncSubscribe(UserProfileState::user)
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


    companion object {
        fun newInstance(userId: Long): UserProfileFragment =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        MvRx.KEY_ARG,
                        UserProfileArgs(id)
                    )
                }
            }
    }
}
