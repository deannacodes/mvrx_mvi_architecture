package com.deanna.mvrx.util

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

class RobolectricApplication : Application()

@RunWith(AndroidJUnit4::class)
@Config(application = RobolectricApplication::class, manifest = Config.NONE)
abstract class RobolectricTest {
    val context: Context get() = ApplicationProvider.getApplicationContext<Context>()
}
