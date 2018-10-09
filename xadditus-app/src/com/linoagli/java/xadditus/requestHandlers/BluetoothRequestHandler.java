/**
 * xadditus App Project.
 * com.linoagli.java.xadditus.requestHandlers
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/22/15 7:10 PM
 */
package com.linoagli.java.xadditus.requestHandlers;

import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import com.linoagli.java.library.Net.Bluetooth.BluetoothServer;

import java.util.StringTokenizer;

public class BluetoothRequestHandler implements BluetoothServer.Callback
{
    @Override
    public void onStarted(String serviceUUID) {}

    @Override
    public void onWaitingForConnection(String serviceUUID)
    {
        System.out.println("Bluetooth server waiting for connection with service UUID " + serviceUUID);
    }

    @Override
    public void onConnected()
    {
        System.out.println("Bluetooth server connected to a client device...");
    }

    @Override
    public void onDisconnected() {}

    @Override
    public void onStopped(String serviceUUID) {}

    @Override
    public void onPreDataProcessing() {}

    @Override
    public void onPostDataProcessing() {}

    @Override
    public void onDataReceived(BluetoothServer bluetoothServer, String data)
    {
        try
        {
            StringTokenizer st = new StringTokenizer(data, Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            String response = new StringBuilder()
                .append(packetTypeString)
                .append(Constants.STRING_TOKENIZER_DELIMITOR).toString();

            switch (packetType)
            {
                case MouseMove:
                    RequestHandlerEngine.doMouseMove(st);
                    break;

                case MouseScroll:
                    RequestHandlerEngine.doMouseScroll(st);
                    break;

                case MouseLeftDown:
                    RequestHandlerEngine.doMouseLeftDown();
                    break;

                case MouseLeftUp:
                    RequestHandlerEngine.doMouseLeftUp();
                    break;

                case MouseLeftClick:
                    RequestHandlerEngine.doMouseLeftClick();
                    break;

                case MouseRightDown:
                    RequestHandlerEngine.doMouseRightDown();
                    break;

                case MouseRightUp:
                    RequestHandlerEngine.doMouseRightUp();
                    break;

                case MouseRightClick:
                    RequestHandlerEngine.doMouseRightClick();
                    break;

                case KeyDown:
                    response += RequestHandlerEngine.doKeyDown(st);
                    bluetoothServer.respond(response);
                    break;

                case KeyUp:
                    RequestHandlerEngine.doKeyUp(st);
                    break;

                case AsciiKeyDown:
                    response += RequestHandlerEngine.doAsciiKeyDown(st);
                    bluetoothServer.respond(response);
                    break;

                case AsciiKeyUp:
                    RequestHandlerEngine.doAsciiKeyUp(st);
                    break;

                case TerminalCommand:
                    RequestHandlerEngine.doTerminalCommand(st, null, bluetoothServer);
                    break;

                case OperatingSystemVolumeUp:
                    RequestHandlerEngine.doOperatingSystemVolumeUp();
                    break;

                case OperatingSystemVolumeDown:
                    RequestHandlerEngine.doOperatingSystemVolumeDown();
                    break;

                case AltF4:
                    RequestHandlerEngine.doAltF4();
                    break;

                case LaunchTerminal:
                    RequestHandlerEngine.doLaunchCommandPrompt();
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
