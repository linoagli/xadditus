/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.base
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.base

import android.support.v7.app.AppCompatActivity
import com.linoagli.android.xadditus.Runtime
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        Runtime.isAppInForeground = true
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        Runtime.isAppInForeground = false
        super.onPause()
    }
}