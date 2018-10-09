/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Services
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/23/15 10:02 PM
 */
package com.linoagli.android.xadditus.processes;

import android.bluetooth.BluetoothDevice;
import com.linoagli.android.xadditus.connections.BluetoothConnection;
import com.linoagli.android.xadditus.connections.TCPConnection;
import com.linoagli.comprotocols.udp.UDPSender;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.Device;
import com.linoagli.java.XadditusCore.PacketEncoding;

public class ConnectionsManager {
    private Device device;
    private TCPConnection tcpConnection;
    private BluetoothConnection bluetoothConnection;
    private DataSyncWorker dataSyncWorker;

    public Device getDevice() {
        return device;
    }

    public boolean isServiceActive() {
        boolean isTcpConnected = tcpConnection != null && tcpConnection.isConnected();
        boolean isBluetoothConnected = bluetoothConnection != null && bluetoothConnection.isConnected();
        return isTcpConnected || isBluetoothConnected;
    }

    public boolean startService(Device device) {
        stopService();

        this.device = device;

        if (device.address != null) {
            tcpConnection = new TCPConnection();
            tcpConnection.init(device.address);
        }

        if (device.data != null) {
            bluetoothConnection = new BluetoothConnection();
            bluetoothConnection.init((BluetoothDevice) device.data);
        }

        dataSyncWorker = new DataSyncWorker();
        dataSyncWorker.start();

        try {
            Thread.sleep(2000L);
        } catch (Exception e) {
            //
        }

        return isServiceActive();
    }

    public void stopService() {
        if (dataSyncWorker != null) {
            dataSyncWorker.cancel();
            dataSyncWorker = null;
        }

        if (bluetoothConnection != null) {
            bluetoothConnection.cleanUp();
            bluetoothConnection = null;
        }

        if (tcpConnection != null) {
            tcpConnection.cleanUp();
            tcpConnection = null;
        }
    }

    public void postRequest(String request, boolean preferTCP) {
        if (tcpConnection != null) {
            if (preferTCP) {
                tcpConnection.postRequest(request);
            } else {
                UDPSender.send(device.address, Constants.SOCKET_PORT_UDP, request);
            }
        }

        if (bluetoothConnection != null) {
            bluetoothConnection.postRequest(request);
        }
    }

    private class DataSyncWorker extends Thread {
        private boolean runLoop = true;

        @Override
        public void run() {
            while (runLoop) {
                try {
                    String request = new StringBuilder()
                        .append(PacketEncoding.PacketType.DataSync.toString())
                        .append(Constants.STRING_TOKENIZER_DELIMITOR)
                        .toString();

                    if (tcpConnection != null) {
                        tcpConnection.postRequest(request);
                    }

                    if (bluetoothConnection != null) {
                        bluetoothConnection.postRequest(request);
                    }
                } catch (Exception e) {
                    //
                }

                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    //
                }
            }
        }

        public void cancel() {
            runLoop = false;
        }
    }
}
