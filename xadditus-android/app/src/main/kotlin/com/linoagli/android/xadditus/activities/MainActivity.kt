/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.activities
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.activities

import android.content.Intent
import android.os.Bundle
import com.linoagli.android.xadditus.Runtime
import com.linoagli.android.xadditus.activities.input.InputInterfaceActivity
import com.linoagli.android.xadditus.base.BaseActivity
import com.linoagli.android.xadditus.services.AutoConnectService

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this@MainActivity, AutoConnectService::class.java))

        val activityClass = if (Runtime.connectionsManager.isServiceActive) InputInterfaceActivity::class.java else DeviceScanActivity::class.java
        startActivity(Intent(this@MainActivity, activityClass))

        finish()
    }
}