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

            StringBuilder sb = new StringBuilder();
            sb.append(packetTypeString);
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

            String response = null;

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
                    RequestHandlerEngine.doKeyDown(st);
                    break;

                case KeyUp:
                    RequestHandlerEngine.doKeyUp(st);
                    break;

                case AsciiKeyDown:
                    RequestHandlerEngine.doAsciiKeyDown(st);
                    break;

                case AsciiKeyUp:
                    RequestHandlerEngine.doAsciiKeyUp(st);
                    break;

//                case RemoteDesktopScreenCapture:
//                    RequestHandlerEngine.doRemoteDesktopScreenCapture(sb);
//                    bluetoothServer.respond(sb.toString());
//                    break;
//
//                case RemoteDesktopTouchDown:
//                    RequestHandlerEngine.doRemoteDesktopTouchDown(st);
//                    break;
//
//                case RemoteDesktopTouchUp:
//                    RequestHandlerEngine.doRemoteDesktopTouchUp(st);
//                    break;
//
//                case RemoteDesktopTouchMove:
//                    RequestHandlerEngine.doRemoteDesktopTouchMove(st);
//                    break;

//                case AudioOutputList:
//                    response = RequestHandlerEngine.doAudioOutputList();
//                    bluetoothServer.respond(response);
//                    break;
//
//                case SetAudioOutPut:
//                    RequestHandlerEngine.doSetAudioOutPut(st);
//                    break;
//
//                case AudioData:
//                    RequestHandlerEngine.doAudioData(st);
//                    break;

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
