/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by Olubusayo K. Faye-Lino Agli on 6/7/15.
 * xadditus App Project.
 */
package com.linoagli.java.xadditus;

import com.linoagli.comprotocols.tcp.TCPServer;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.library.Net.Bluetooth.BluetoothServer;
import com.linoagli.java.library.Net.UDP.UDPServer;
import com.linoagli.java.library.Process.Shell;
import com.linoagli.java.xadditus.requestHandlers.BluetoothRequestHandler;
import com.linoagli.java.xadditus.requestHandlers.TCPRequestHandler;
import com.linoagli.java.xadditus.requestHandlers.UDPRequestHandler;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import java.util.UUID;

public class Services {
    public static UDPServer udpServer;
    public static TCPServer tcpServer;
    public static BluetoothServer bluetoothServer;
    public static AudioPlaybackManager audioPlaybackManager;
    public static Shell shell;

    public static void start() {
        stop();

        System.out.println("Starting connections services...");

        udpServer = new UDPServer(Constants.SOCKET_PORT_UDP, new UDPRequestHandler());
        udpServer.setDataPacketSize(10 * 1024);
        udpServer.start();

        tcpServer = new TCPServer(new TCPRequestHandler());
        tcpServer.start(Constants.SOCKET_PORT_TCP_MAIN);

        try {
            if (LocalDevice.getLocalDevice() != null) {
                bluetoothServer = new BluetoothServer(new BluetoothRequestHandler());
                bluetoothServer.start(UUID.fromString(Constants.BLUETOOTH_SERVICE_UUID).toString());
            }
        }
        catch (BluetoothStateException e) {
            System.err.println("Failed to start the bluetooth server...");
        }

        audioPlaybackManager = new AudioPlaybackManager();
        audioPlaybackManager.init();

        shell = new Shell();
    }

    public static void stop() {
        System.out.println("Stopping connections services...");

        shell = null;

        if (audioPlaybackManager != null) {
            audioPlaybackManager.cleanUp();
            audioPlaybackManager = null;
        }

        if (bluetoothServer != null) {
            bluetoothServer.stop();
            bluetoothServer = null;
        }

        if (tcpServer != null) {
            tcpServer.stop();
            tcpServer = null;
        }

        if (udpServer != null) {
            udpServer.stop();
            udpServer = null;
        }
    }
}
