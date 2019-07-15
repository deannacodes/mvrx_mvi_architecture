package com.deanna.mvrx.di

import com.deanna.mvrx.ui.MainActivity
import com.deanna.mvrx.ui.users.UsersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindUsersFragment(): UsersFragment

//    @ContributesAndroidInjector
//    abstract fun bindUserProfileFragment(): UserProfileFragment
}
