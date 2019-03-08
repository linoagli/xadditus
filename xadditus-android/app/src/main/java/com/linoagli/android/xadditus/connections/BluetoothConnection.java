/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Services
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/23/15 12:36 PM
 */
package com.linoagli.android.xadditus.connections;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.linoagli.android.xadditus.DataReceivedEvent;
import com.linoagli.java.XadditusCore.Constants;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

public class BluetoothConnection {
    private BluetoothSocket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void init(BluetoothDevice device) {
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Constants.BLUETOOTH_SERVICE_UUID));
            socket.connect();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new StreamReader().start(); // TODO: Don't really like loose threads but this one should only live as long as the input stream is open
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            in = null;
        }

        if (out != null) {
            out.close();
            out = null;
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            socket = null;
        }
    }

    public void postRequest(String request) {
        out.println(request);
    }

    public boolean isConnected() {
        return in != null && out != null && socket != null && socket.isConnected();
    }

    private class StreamReader extends Thread {
        @Override
        public void run() {
            String line;

            try {
                while ((line = in.readLine()) != null) {
                    EventBus.getDefault().post(new DataReceivedEvent(line));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cleanUp();
            }
        }
    }
}
