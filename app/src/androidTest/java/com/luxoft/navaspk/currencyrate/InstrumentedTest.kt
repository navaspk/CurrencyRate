package com.luxoft.navaspk.currencyrate


import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.luxoft.navaspk.currencyrate", appContext.packageName)
    }

    @Test
    fun validate_total_item_in_list() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val count = CurrencyRateAdapter(appContext ,null).itemCount
        assertThat("total number of item is wrong", 2, CoreMatchers.equalTo(count))
    }
}
