/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.receivers
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.linoagli.android.xadditus.AppRuntime;
import com.linoagli.android.xadditus.Constants;
import com.linoagli.android.xadditus.DeviceFoundEvent;
import com.linoagli.android.xadditus.utils.SharedPrefsHelper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AutoConnectService extends Service {
    private AutoConnectWorker autoConnectWorker = new AutoConnectWorker();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
        autoConnectWorker.start();
    }

    @Override
    public void onDestroy() {
        autoConnectWorker.cancel();
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceFoundEvent event) {
        if (AppRuntime.isAppInForeground) {
            return;
        }

        String targetDeviceName = SharedPrefsHelper.load(this, Constants.PREFS_LAST_SELECTED_DEVICE, null);

        if (targetDeviceName == null || event.getDevice().address == null || !targetDeviceName.equals(event.getDevice().name)) {
            return;
        }

        AppRuntime.deviceScanner.stopScan();

        boolean didStartService = AppRuntime.connectionsManager.startService(event.getDevice());

        if (didStartService) {
            startService(new Intent(this, ConnectionService.class));
        }
    }

    private class AutoConnectWorker extends Thread {
        private boolean runLoop = true;

        @Override
        public void run() {
            while (runLoop) {
                try {
                    boolean shouldScan =
                        !AppRuntime.connectionsManager.isServiceActive() &&
                        !AppRuntime.deviceScanner.isScanning();

                    if (shouldScan) {
                        System.out.println("AutoConnectService scanning for devices...");

                        AppRuntime.deviceScanner.startScan(getApplicationContext(), true, false);

                        try {
                            Thread.sleep(1000L * 20);
                        } catch (Exception e) {
                            //
                        }

                        AppRuntime.deviceScanner.stopScan();
                    }
                } catch (Exception e) {
                    //
                }

                try {
                    Thread.sleep(1000L * 30);
                } catch (Exception e) {
                    //
                }
            }
        }

        void cancel() {
            runLoop = false;
        }
    }
}