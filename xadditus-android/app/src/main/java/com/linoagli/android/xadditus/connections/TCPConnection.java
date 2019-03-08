/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Services
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/23/15 12:36 PM
 */
package com.linoagli.android.xadditus.connections;

import com.linoagli.android.xadditus.DataReceivedEvent;
import com.linoagli.comprotocols.DataPacket;
import com.linoagli.comprotocols.tcp.TCPClient;
import com.linoagli.java.XadditusCore.Constants;
import org.greenrobot.eventbus.EventBus;

import java.net.InetAddress;

public class TCPConnection implements TCPClient.Callback {
    private TCPClient tcpClient;

    @Override
    public void onConnected(InetAddress serverIp, int port) {
        System.out.println("TCP client connected to server " + serverIp.toString() + " on port " + port);
    }

    @Override
    public void onConnectionFailed(InetAddress serverIp, int port) {
        System.out.println("TCP client failed to connected to server " + serverIp.toString() + " on port " + port);
    }

    @Override
    public void onDisconnected() {
        System.out.println("TCP client disconnected from server.");
    }

    @Override
    public void onDataReceived(DataPacket dataPacket) {
        EventBus.getDefault().post(new DataReceivedEvent(dataPacket.data));
    }

    public void init(InetAddress deviceIp) {
        tcpClient = new TCPClient(this);
        tcpClient.connect(deviceIp, Constants.SOCKET_PORT_TCP_MAIN);
    }

    public void cleanUp() {
        if (tcpClient != null) {
            tcpClient.disconnect();
            tcpClient = null;
        }
    }

    public void postRequest(String request) {
        tcpClient.query(request);
    }

    public boolean isConnected() {
        return tcpClient != null && tcpClient.isRunning();
    }
}
