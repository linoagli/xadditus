/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.DeviceScan
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/22/15 8:26 PM
 */
package com.linoagli.android.xadditus.scanners;

import android.content.Context;
import com.linoagli.android.xadditus.DeviceFoundEvent;
import com.linoagli.android.xadditus.helpers.Utils;
import com.linoagli.comprotocols.DataPacket;
import com.linoagli.comprotocols.udp.UDPListener;
import com.linoagli.comprotocols.udp.UDPSender;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.Device;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;

import java.net.InetAddress;
import java.util.StringTokenizer;

public class UDPScanner implements UDPListener.Callback {
    private UDPListener udpServer;
    private Context context;

    public UDPScanner(Context context) {
        this.context = context;
    }

    @Override
    public void onStarted(int port) {
        System.out.println("UDP listener connected to port " + port);
        System.out.println("Performing initial scan of the network for devices");
    }

    @Override
    public void onStopping() {
        System.out.println("UDP listener stopping...");
    }

    @Override
    public void onDataReceived(DataPacket dataPacket) {
        try {
            StringTokenizer st = new StringTokenizer(dataPacket.data, Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            if (packetType == PacketEncoding.PacketType.ScanResponse) {
                String name = st.nextToken();
                String osName = st.nextToken();
                String osVersion = st.nextToken();

                final Device device = new Device(name, osName, osVersion, dataPacket.address, Utils.INSTANCE.getConnectionSSID(context));

                System.out.println("Network device found: " + name + "; " + osName + "; " + osVersion);
                EventBus.getDefault().post(new DeviceFoundEvent(device));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        udpServer = new UDPListener(Constants.SOCKET_PORT_UDP, this);
        udpServer.start();
    }

    public void cleanUp() {
        if (udpServer != null) {
            udpServer.stop();
            udpServer = null;
        }
    }

    public void scan() {
        System.out.println("Attempting to scan the current networked for devices...");

        try {
            InetAddress broadcastAddress = InetAddress.getByName(Utils.INSTANCE.getBroadcastIpAddress(context));

            StringBuilder sb = new StringBuilder();
            sb.append(PacketEncoding.PacketType.Scan.toString());
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

            UDPSender.send(broadcastAddress, Constants.SOCKET_PORT_UDP, sb.toString());
            UDPSender.send(broadcastAddress, Constants.SOCKET_PORT_UDP, sb.toString());
            UDPSender.send(broadcastAddress, Constants.SOCKET_PORT_UDP, sb.toString());
        } catch (Exception e) {
            System.out.println("Network scan failed");
            e.printStackTrace();
        }
    }
}
