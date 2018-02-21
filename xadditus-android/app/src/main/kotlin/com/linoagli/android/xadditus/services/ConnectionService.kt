/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.services
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.services

import android.app.Service
import android.content.Intent
import com.linoagli.android.xadditus.ConnectionInactiveDetectedEvent
import com.linoagli.android.xadditus.DataReceivedEvent
import com.linoagli.android.xadditus.Runtime
import com.linoagli.android.xadditus.SendRequestEvent
import com.linoagli.android.xadditus.helpers.NotificationFactory
import com.linoagli.java.XadditusCore.Constants
import com.linoagli.java.XadditusCore.PacketEncoding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ConnectionService : Service() {
    private val NOTIFICATION_ID = 0x8930
    private val connectionMonitorThread = ConnectionMonitorThread()

    override fun onCreate() {
        super.onCreate()

        EventBus.getDefault().register(this@ConnectionService)
        connectionMonitorThread.start()
    }

    override fun onDestroy() {
        connectionMonitorThread.cancel()
        EventBus.getDefault().unregister(this@ConnectionService)

        super.onDestroy()
    }

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, NotificationFactory.createConnectionServiceNotification(this@ConnectionService))
        return Service.START_STICKY
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: SendRequestEvent) {
        Runtime.connectionsManager.postRequest(event.request)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEvent(event: DataReceivedEvent) {
        try {
            val st = StringTokenizer(event.data, Constants.STRING_TOKENIZER_DELIMITOR)
            val packetTypeString = st.nextToken()
            val packetType = PacketEncoding.PacketType.valueOf(packetTypeString)

            if (packetType == PacketEncoding.PacketType.DataSync) {

            }
        } catch (e: Exception) {
            //
        }
    }

    private inner class ConnectionMonitorThread : Thread() {
        private var runLoop = true

        override fun run() {
            while (runLoop) {
                try {
                    if (!Runtime.connectionsManager.isServiceActive) {
                        EventBus.getDefault().post(ConnectionInactiveDetectedEvent())
                        stopSelf()
                    }
                } catch (e: Exception) {
                    //
                }

                Thread.sleep(1000L * 3)
            }
        }

        fun cancel() {
            runLoop = false
        }
    }
}