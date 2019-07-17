package com.deanna.mvrx.mvibase

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.*
import com.deanna.mvrx.R
import com.deanna.mvrx.ui.users.UserListIntent
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable


abstract class BaseFragment : BaseMvRxFragment() {

    private lateinit var recyclerView: EpoxyRecyclerView
    protected lateinit var searchView: SearchView
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected val epoxyController by lazy { epoxyController() }


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.clearDisappearingChildren();
        return inflater.inflate(R.layout.main_fragment, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            swipeRefreshLayout = findViewById(R.id.users_swipe_refresh_layout)
            searchView = findViewById(R.id.user_list_search_view)
            recyclerView.setController(epoxyController)
        }
    }

    abstract fun epoxyController(): MvRxEpoxyController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }


    override fun invalidate() {
        recyclerView.requestModelBuild()
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }

    protected fun navigateTo(@IdRes actionId: Int, arg: Parcelable? = null) {

        val bundle = arg?.let { Bundle().apply { putParcelable(MvRx.KEY_ARG, it) } }
        findNavController().navigate(actionId, bundle)

    }
}