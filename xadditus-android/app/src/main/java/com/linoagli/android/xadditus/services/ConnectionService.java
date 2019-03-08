/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.services
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.linoagli.android.xadditus.*;
import com.linoagli.android.xadditus.activities.DeviceScanActivity;
import com.linoagli.android.xadditus.activities.InputInterfaceActivity;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;
import java.util.StringTokenizer;

public class ConnectionService extends Service {
    private static final int NOTIFICATION_ID = 0x8930;

    private ConnectionMonitorThread connectionMonitorThread = new ConnectionMonitorThread();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
        connectionMonitorThread.start();
    }

    @Override
    public void onDestroy() {
        connectionMonitorThread.cancel();
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return Service.START_STICKY;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendRequestEvent event) {
        AppRuntime.connectionsManager.postRequest(event.getRequest(), event.getPreferTcp());
    }

    private Notification createNotification() {
        String title = String.format(Locale.getDefault(), getString(R.string.string_connectedTo), AppRuntime.connectionsManager.getDevice().name);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
            .addParentStack(DeviceScanActivity.class)
            .addNextIntent(new Intent(this, InputInterfaceActivity.class));

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent volumeUpIntent = PendingIntent.getBroadcast(this, 0,
            new Intent(com.linoagli.android.xadditus.Constants.INTENT_ACTION_REQUEST_INCREASE_SYSTEM_VOLUME),
            PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent volumeDownIntent = PendingIntent.getBroadcast(this, 0,
            new Intent(com.linoagli.android.xadditus.Constants.INTENT_ACTION_REQUEST_DECREASE_SYSTEM_VOLUME),
            PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, "xadditus")
            .setContentTitle(title)
            .setContentIntent(contentIntent)
            .setShowWhen(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_notification_connected)
            .addAction(R.drawable.ic_volume_up_black_24dp, getString(R.string.button_volumeUp), volumeUpIntent)
            .addAction(R.drawable.ic_volume_down_black_24dp, getString(R.string.button_volumeDown), volumeDownIntent)
            .build();
    }

    private class ConnectionMonitorThread extends Thread {
        private boolean runLoop = true;

        @Override
        public void run() {
            while (runLoop) {
                try {
                    if (!AppRuntime.connectionsManager.isServiceActive()) {
                        EventBus.getDefault().post(new ConnectionInactiveDetectedEvent());
                        stopSelf();
                    }
                } catch (Exception e) {
                    //
                }

                try {
                    Thread.sleep(1000L * 3);
                }
                catch (InterruptedException e) {
                    //
                }
            }
        }

        void cancel() {
            runLoop = false;
        }
    }
}