/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.broadcastReceivers
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.linoagli.android.xadditus.Constants
import com.linoagli.android.xadditus.SendRequestEvent
import com.linoagli.android.xadditus.helpers.RequestFactory
import org.greenrobot.eventbus.EventBus

class NotificationBroadcastReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.INTENT_ACTION_REQUEST_INCREASE_SYSTEM_VOLUME -> {
                val request = RequestFactory.createSystemVolumeUpRequest()
                EventBus.getDefault().post(SendRequestEvent(request))
            }

            Constants.INTENT_ACTION_REQUEST_DECREASE_SYSTEM_VOLUME -> {
                val request = RequestFactory.createSystemVolumeDownRequest()
                EventBus.getDefault().post(SendRequestEvent(request))
            }
        }
    }
}