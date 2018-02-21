/**
 * xadditus-app Project.
 * com.linoagli.java.xadditus.requestHandlers
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 */
package com.linoagli.java.xadditus.requestHandlers;

import com.linoagli.comprotocols.DataPacket;
import com.linoagli.comprotocols.tcp.TCPServer;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;

import java.util.StringTokenizer;

public class TCPRequestHandler implements TCPServer.Callback {
    @Override
    public void onWaitingForConnection(int port) {
        System.out.println("TCP server waiting for connection on port " + port);
    }

    @Override
    public void onConnected(TCPServer.Connection connection) {
        System.out.println("TCP server connected to device " + connection.getRemoteHostAddress().toString());
    }

    @Override
    public void onDataReceived(TCPServer.Connection connection, DataPacket dataPacket) {
        try {
            StringTokenizer st = new StringTokenizer(dataPacket.data, Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            String response = "";

            switch (packetType) {
                case DataSync:
                    response = RequestHandlerEngine.doDataSync();
                    break;
            }

            connection.respond(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
