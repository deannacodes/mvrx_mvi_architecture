package com.deanna.mvrx.di

import android.content.Context
import com.deanna.mvrx.MvRxApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppAssistedModule::class,
        RetrofitModule::class,
        ActivityFragmentBuilder::class,
        AppModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: MvRxApplication)

    override fun inject(instance: DaggerApplication?)
}
