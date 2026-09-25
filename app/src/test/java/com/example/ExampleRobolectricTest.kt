package com.example

import android.content.Context
import android.os.BatteryManager
import androidx.test.core.app.ApplicationProvider
import com.example.model.ChargingSource
import com.example.model.ChargingTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Charge FX", appName)
    }

    @Test
    fun `verify charging themes count is at least 30`() {
        val themes = ChargingTheme.entries
        assertTrue("Theme gallery must contain at least 30 animations", themes.size >= 30)
    }

    @Test
    fun `verify charging source detection mapping`() {
        val usbSource = ChargingSource.fromPlugged(BatteryManager.BATTERY_PLUGGED_USB, isCharging = true)
        assertEquals(ChargingSource.USB, usbSource)

        val acSource = ChargingSource.fromPlugged(BatteryManager.BATTERY_PLUGGED_AC, isCharging = true)
        assertEquals(ChargingSource.AC, acSource)

        val wirelessSource = ChargingSource.fromPlugged(BatteryManager.BATTERY_PLUGGED_WIRELESS, isCharging = true)
        assertEquals(ChargingSource.WIRELESS, wirelessSource)

        val unplugged = ChargingSource.fromPlugged(0, isCharging = false)
        assertEquals(ChargingSource.NONE, unplugged)
    }
}
