/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.DeviceScan
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/22/15 8:27 PM
 */
package com.linoagli.android.xadditus.scanners;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import com.linoagli.android.xadditus.DeviceFoundEvent;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.Device;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothScanner {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;

    private List<BluetoothDevice> pairedDevices = new ArrayList<>();

    private boolean hasBluetooth = false;

    public BluetoothScanner(Context context) {
        this.context = context;
    }

    public void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        hasBluetooth = bluetoothAdapter != null && bluetoothAdapter.isEnabled();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        context.registerReceiver(receiver, intentFilter);
    }

    public void cleanUp() {
        context.unregisterReceiver(receiver);

        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter = null;
    }

    public void scan() {
        if (!hasBluetooth) return;

        System.out.println("Starting bluetooth device discovery...");

        // Cancel ongoing discovery
        if (bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();

        // Retrieving paired devices
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        pairedDevices.clear();

        for (BluetoothDevice bluetoothDevice : bondedDevices) {
            Device device = new Device(bluetoothDevice.getName(), "Bluetooth", "", null, "N/A");
            device.data = bluetoothDevice;

            pairedDevices.add(bluetoothDevice);
            EventBus.getDefault().post(new DeviceFoundEvent(device));
        }

        // Discovering devices
        bluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            System.out.println("intent action: " + intent.getAction());

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                System.out.println("Bluetooth device discovery started...");
            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                System.out.println("Bluetooth device discovery finished...");

                while (!pairedDevices.isEmpty()) {
                    BluetoothDevice bluetoothDevice = pairedDevices.remove(0);

                    System.out.println("Discovering services for device: " + bluetoothDevice.getName());
                    if (!bluetoothDevice.fetchUuidsWithSdp()) {
                        System.out.println("SDP UUID fetch failed for " + bluetoothDevice.getName());
                    }
                }
            }

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                System.out.println("bluetooth device found: " + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());

                String deviceName = bluetoothDevice.getName();

                if (deviceName != null && !deviceName.trim().isEmpty()) {
                    Device device = new Device(deviceName, "Bluetooth", "N/A", null, "N/A");
                    device.data = bluetoothDevice;

                    EventBus.getDefault().post(new DeviceFoundEvent(device));
                }
            }

            if (action.equals(BluetoothDevice.ACTION_UUID)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] parcelUUIDs = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);

                System.out.println("Service inquiry result received for device " + bluetoothDevice.getName());

                if (parcelUUIDs != null) {
                    for (Parcelable item : parcelUUIDs) {
                        String uuid = item.toString();

                        System.out.println("Service info: Device=" + bluetoothDevice.getName() + ", UUID=" + uuid);

                        if (Constants.BLUETOOTH_SERVICE_UUID_AS_SEEN_BY_ANDROID.equals(uuid)) {
                            System.out.println(bluetoothDevice.getName() + " is a Xadditus device!");
                        }
                    }
                }
            }
        }
    };
}
