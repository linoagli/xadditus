/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.helpers
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.helpers

import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.linoagli.android.xadditus.Constants
import com.linoagli.android.xadditus.R
import com.linoagli.android.xadditus.Runtime
import com.linoagli.android.xadditus.activities.input.InputInterfaceActivity
import com.linoagli.android.xadditus.activities.DeviceScanActivity
import java.util.*

object NotificationFactory {
    fun createConnectionServiceNotification(context: Context): Notification {
        val title = String.format(Locale.getDefault(),
                context.getString(R.string.string_connectedTo),
                Runtime.connectionsManager.device.name)

        val stackBuilder = TaskStackBuilder.create(context)
                .addParentStack(DeviceScanActivity::class.java)
                .addNextIntent(Intent(context, InputInterfaceActivity::class.java))
        val contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val volumeUpIntent = PendingIntent.getBroadcast(context, 0,
                Intent(Constants.INTENT_ACTION_REQUEST_INCREASE_SYSTEM_VOLUME), PendingIntent.FLAG_UPDATE_CURRENT)

        val volumeDownIntent = PendingIntent.getBroadcast(context, 0,
                Intent(Constants.INTENT_ACTION_REQUEST_DECREASE_SYSTEM_VOLUME), PendingIntent.FLAG_UPDATE_CURRENT)

        val altF4Intent = PendingIntent.getBroadcast(context, 0,
                Intent(Constants.INTENT_ACTION_REQUEST_ALT_F4), PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentIntent(contentIntent)
                .setShowWhen(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_notification_connected)
                .addAction(R.drawable.ic_volume_up_black_24dp, context.getString(R.string.button_volumeUp), volumeUpIntent)
                .addAction(R.drawable.ic_volume_down_black_24dp, context.getString(R.string.button_volumeDown), volumeDownIntent)
                .addAction(R.drawable.ic_close_black_24dp, context.getString(R.string.button_altF4), altF4Intent)
                .build()
    }
}