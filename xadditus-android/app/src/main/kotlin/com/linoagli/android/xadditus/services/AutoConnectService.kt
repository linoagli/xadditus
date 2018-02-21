/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.services
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.services

import android.app.Service
import android.content.Intent
import com.linoagli.android.xadditus.Constants
import com.linoagli.android.xadditus.Runtime
import com.linoagli.android.xadditus.helpers.SharedPrefsHelper
import com.linoagli.android.xadditus.DeviceFoundEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AutoConnectService : Service() {
    private val autoConnectWorker = AutoConnectWorker()

    override fun onCreate() {
        super.onCreate()

        EventBus.getDefault().register(this@AutoConnectService)
        autoConnectWorker.start()
    }

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = Service.START_STICKY

    override fun onDestroy() {
        autoConnectWorker.cancel()
        EventBus.getDefault().unregister(this@AutoConnectService)

        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: DeviceFoundEvent) {
        if (Runtime.isAppInForeground) {
            return;
        }

        val targetDevice = SharedPrefsHelper.load(this@AutoConnectService, Constants.PREFS_LAST_SELECTED_DEVICE, null)

        if (targetDevice == null || event.device.address == null || targetDevice != event.device.name) {
            return
        }

        Runtime.deviceScanner.stopScan()

        var didStartService = Runtime.connectionsManager.startService(event.device)

        if (didStartService) {
            startService(Intent(this@AutoConnectService, ConnectionService::class.java))
        }
    }

    private inner class AutoConnectWorker : Thread() {
        private var runLoop = true

        override fun run() {
            while (runLoop) {
                try {
                    val shouldScan = !Runtime.connectionsManager.isServiceActive && !Runtime.deviceScanner.isScanning

                    if (shouldScan) {
                        println("AutoConnectService scanning for devices...")

                        Runtime.deviceScanner.startScan(this@AutoConnectService)
                        Thread.sleep(1000L * 20)
                        Runtime.deviceScanner.stopScan()
                    }
                } catch (e: Exception) {
                    //
                }

                Thread.sleep(1000L * 60 * 3)
            }
        }

        fun cancel() {
            runLoop = false
        }
    }
}