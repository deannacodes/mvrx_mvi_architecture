package com.deanna.mvrx.di

import com.deanna.mvrx.database.DatabaseController
import com.deanna.mvrx.database.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @JvmStatic
    @Provides
    @Singleton
    fun providesUserDao(controller: DatabaseController): UserDao = controller.database.userDao()
}