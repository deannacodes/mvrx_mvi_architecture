package com.deanna.mvrx.ui.users

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.deanna.mvrx.R
import com.deanna.mvrx.mvibase.BaseFragment
import com.deanna.mvrx.mvibase.simpleController
import com.deanna.mvrx.ui.views.basicRow
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UsersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: UsersViewModel.Factory
    private val viewModel: UsersViewModel by fragmentViewModel()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.asyncSubscribe(UsersState::users)
    }

    override fun epoxyController() = simpleController(viewModel) { state ->


        val list = state.users.invoke()

        list?.forEach { user ->
            basicRow {
                id(user.userId.toString())
                title(user.userName)
                subtitle("Rep: " + user.reputation.toString())
                image(user.imageUrl)
//                clickListener { _ ->
//                    navigateTo(
//                        R.id.action_dadJokeIndex_to_dadJokeDetailFragment,
//                        DadJokeDetailArgs(joke.id)
//                    )
//                }
            }
        }

//        loadingRow {
//            // Changing the ID will force it to rebind when new data is loaded even if it is
//            // still on screen which will ensure that we trigger loading again.
//            id("loading${state.users.invoke()?.size}")
//            onBind { _, _, _ -> viewModel.fetchUsers() }
//        }
    }

}


fun Any.logError(throwable: Throwable, message: String = "") = Log.e(javaClass.simpleName, message, throwable)