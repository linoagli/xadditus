/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.DeviceScan.Handlers
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/22/15 9:11 PM
 */
package com.linoagli.android.xadditus.processes;

import android.content.Context;
import com.linoagli.android.xadditus.DeviceScanStartedEvent;
import com.linoagli.android.xadditus.scanners.BluetoothScanner;
import com.linoagli.android.xadditus.scanners.UDPScanner;
import com.linoagli.android.xadditus.utils.NetworkUtils;
import org.greenrobot.eventbus.EventBus;

public class DeviceScanner {
    private UDPScanner udpScanner;
    private BluetoothScanner bluetoothScanner;

    private boolean isScanning = false;

    public boolean isScanning() {
        return isScanning;
    }

    public void startScan(Context context) {
        startScan(context, true, true);
    }

    public void startScan(Context context, boolean scanWifi, boolean scanBluetooth) {
        stopScan();

        isScanning = true;

        EventBus.getDefault().post(new DeviceScanStartedEvent());

        if (scanWifi && NetworkUtils.isConnectedToWifi(context)) {
            udpScanner = new UDPScanner(context);
            udpScanner.init();
            udpScanner.scan();
        }

        if (scanBluetooth && NetworkUtils.isConnectedToBluetooth()) {
            bluetoothScanner = new BluetoothScanner(context);
            bluetoothScanner.init();
            bluetoothScanner.scan();
        }
    }

    public void stopScan() {
        if (udpScanner != null) {
            udpScanner.cleanUp();
            udpScanner = null;
        }

        if (bluetoothScanner != null) {
            bluetoothScanner.cleanUp();
            bluetoothScanner = null;
        }

        isScanning = false;
    }
}
