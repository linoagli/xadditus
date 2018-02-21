/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by linoagli on 2/5/15.
 * xadditus App Project.
 */
package com.linoagli.java.xadditus.requestHandlers;

import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import com.linoagli.java.library.Net.DataPacket;
import com.linoagli.java.library.Net.UDP.UDPSender;
import com.linoagli.java.library.Net.UDP.UDPServer;

import java.util.StringTokenizer;

public class UDPRequestHandler implements UDPServer.Callback {
    @Override
    public void onStarted(int port) {
    }

    @Override
    public void onConnected(int port) {
        System.out.println("UDP server connected to port " + String.valueOf(port));
    }

    @Override
    public void onConnectionFailed(int port) {
        System.out.println("UDP server connection failed on port " + String.valueOf(port));
    }

    @Override
    public void onStopped(int port) {
    }

    @Override
    public void onPreDataProcessing() {
    }

    @Override
    public void onPostDataProcessing() {
    }

    @Override
    public void onDataReceived(DataPacket dataPacket) {
        try {
            StringTokenizer st = new StringTokenizer(dataPacket.data, Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            String response = null;

            switch (packetType) {
                case Scan:
                    response = RequestHandlerEngine.doScan();
                    UDPSender.send(dataPacket.address, dataPacket.port, response);
                    break;

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

//                case KeyDown:
//                    RequestHandlerEngine.doKeyDown(st);
//                    break;
//
//                case KeyUp:
//                    RequestHandlerEngine.doKeyUp(st);
//                    break;
//
//                case AsciiKeyDown:
//                    RequestHandlerEngine.doAsciiKeyDown(st);
//                    break;
//
//                case AsciiKeyUp:
//                    RequestHandlerEngine.doAsciiKeyUp(st);
//                    break;

//                case RemoteDesktopScreenCapture:
//                    RequestHandlerEngine.doRemoteDesktopScreenCapture(sb);
//                    UDPSender.send(dataPacket.address, Constants.SOCKET_PORT_UDP, sb.toString());
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
//                    UDPSender.send(dataPacket.address, dataPacket.port, response);
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
                    RequestHandlerEngine.doTerminalCommand(st, dataPacket, null);
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
