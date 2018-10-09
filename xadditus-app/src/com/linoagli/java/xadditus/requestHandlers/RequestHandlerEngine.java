/**
 * xadditus App Project.
 * com.linoagli.java.xadditus.requestHandlers
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 11/22/15 7:22 PM
 */
package com.linoagli.java.xadditus.requestHandlers;

import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import com.linoagli.java.library.Net.Bluetooth.BluetoothServer;
import com.linoagli.java.library.Net.DataPacket;
import com.linoagli.java.library.Net.UDP.UDPSender;
import com.linoagli.java.library.SystemInfo;
import com.linoagli.java.xadditus.NirCmdHelper;
import com.linoagli.java.xadditus.Services;
import org.apache.commons.net.util.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class RequestHandlerEngine {
    public static String doScan() {
        String response = null;

        try {
            String hostName = "Device";

            try {
                hostName = InetAddress.getLocalHost().getHostName();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(PacketEncoding.PacketType.ScanResponse.toString());
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
            sb.append(hostName);
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
            sb.append(System.getProperty(SystemInfo.SYSTEM_PROPERTY_OS_NAME));
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
            sb.append(System.getProperty(SystemInfo.SYSTEM_PROPERTY_OS_VERSION));
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

            response = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static void doDataSync() {
        //
    }

    public static void doMouseMove(StringTokenizer st) {
        try {
            float dx = Float.valueOf(st.nextToken());
            float dy = Float.valueOf(st.nextToken());
            boolean showMouse = Boolean.valueOf(st.nextToken());

            Point point_old = MouseInfo.getPointerInfo().getLocation();
            Point point_new = new Point();
            point_new.x = point_old.x + (int) dx;
            point_new.y = point_old.y + (int) dy;

            new Robot().mouseMove(point_new.x, point_new.y);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseScroll(StringTokenizer st) {
        try {
            float dy = Float.valueOf(st.nextToken());
            new Robot().mouseWheel((int) dy);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseLeftDown() {
        try {
            new Robot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseLeftUp() {
        try {
            new Robot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseLeftClick() {
        try {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseRightDown() {
        try {
            new Robot().mousePress(InputEvent.BUTTON3_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseRightUp() {
        try {
            new Robot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doMouseRightClick() {
        try {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doKeyDown(StringTokenizer st) {
        String responsePart = "";
        try {
            PacketEncoding.KeyboardKeys key = PacketEncoding.KeyboardKeys.valueOf(st.nextToken());
            int keyCode = keyboardKeyToCode(key);
            new Robot().keyPress(keyCode);

            responsePart = new StringBuilder()
                .append(key.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .append((char) keyCode)
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return responsePart;
    }

    public static void doKeyUp(StringTokenizer st) {
        try {
            PacketEncoding.KeyboardKeys key = PacketEncoding.KeyboardKeys.valueOf(st.nextToken());
            int keyCode = keyboardKeyToCode(key);

            if (keyCode != -1) new Robot().keyRelease(keyCode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doAsciiKeyDown(StringTokenizer st) {
        String responsePart = "";
        try {
            int keyCode = Integer.valueOf(st.nextToken());
            new Robot().keyPress(keyCode);

            responsePart = new StringBuilder()
                .append((char) keyCode)
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return responsePart;
    }

    public static void doAsciiKeyUp(StringTokenizer st) {
        try {
            int keycode = Integer.valueOf(st.nextToken());
            new Robot().keyRelease(keycode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doRemoteDesktopScreenCapture(StringBuilder sb) {
        try {
            String screenCaptureData = getScreenCaptureData();

            if (screenCaptureData == null) return;

            sb.append(screenCaptureData);
            sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void doRemoteDesktopTouchDown(StringTokenizer st) {
        try {
            double ratio_x = Double.valueOf(st.nextToken());
            double ratio_y = Double.valueOf(st.nextToken());
            Point point = getPointerLocation(ratio_x, ratio_y);

            Robot robot = new Robot();
            robot.mouseMove(point.x, point.y);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doRemoteDesktopTouchUp(StringTokenizer st) {
        try {
            double ratio_x = Double.valueOf(st.nextToken());
            double ratio_y = Double.valueOf(st.nextToken());
            Point point = getPointerLocation(ratio_x, ratio_y);

            Robot robot = new Robot();
            robot.mouseMove(point.x, point.y);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doRemoteDesktopTouchMove(StringTokenizer st) {
        try {
            double ratio_x = Double.valueOf(st.nextToken());
            double ratio_y = Double.valueOf(st.nextToken());
            Point point = getPointerLocation(ratio_x, ratio_y);

            new Robot().mouseMove(point.x, point.y);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doAudioOutputList() {
        try {
            String mixerDetailsData = " ";
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(Services.audioPlaybackManager.getMixerDetails());
                oos.flush();
                oos.close();

                mixerDetailsData = Base64.encodeBase64String(baos.toByteArray(), false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(PacketEncoding.PacketType.AudioOutputList.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
            sb.append(mixerDetailsData).append(Constants.STRING_TOKENIZER_DELIMITOR);
            sb.append(Services.audioPlaybackManager.getCurrMixerIndex()).append(Constants.STRING_TOKENIZER_DELIMITOR);

            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void doSetAudioOutPut(StringTokenizer st) {
        try {
            String mixerIndexString = st.nextToken();
            int mixerIndex = Integer.parseInt(mixerIndexString);

            Services.audioPlaybackManager.setCurrMixerIndex(mixerIndex);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doAudioData(StringTokenizer st) {
        try {
            String audioData = st.nextToken();
            byte[] bytes = Base64.decodeBase64(audioData);

            Services.audioPlaybackManager.write(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doTerminalCommand(StringTokenizer st, DataPacket sourceDataPacket, BluetoothServer bluetoothServer) {
        try {
            String command = st.nextToken();

            Services.shell.setInputStreamReaderCallback((line) -> {
                System.out.println("shell v: " + line);

                StringBuilder sb = new StringBuilder();
                sb.append(PacketEncoding.PacketType.TerminalVerboseOutput.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
                sb.append(line).append(Constants.STRING_TOKENIZER_DELIMITOR);

                if (sourceDataPacket != null)
                    UDPSender.send(sourceDataPacket.address, sourceDataPacket.port, sb.toString());
                if (bluetoothServer != null)
                    bluetoothServer.respond(sb.toString());
            });

            Services.shell.setErrorStreamReaderCallback((line) -> {
                System.out.println("shell e: " + line);

                StringBuilder sb = new StringBuilder();
                sb.append(PacketEncoding.PacketType.TerminalErrorOutput.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
                sb.append(line).append(Constants.STRING_TOKENIZER_DELIMITOR);

                if (sourceDataPacket != null)
                    UDPSender.send(sourceDataPacket.address, sourceDataPacket.port, sb.toString());
                if (bluetoothServer != null)
                    bluetoothServer.respond(sb.toString());
            });

            Services.shell.run(command);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doOperatingSystemVolumeUp() {
        try {
            if (SystemInfo.isWindows()) {
                String nircmdPath = NirCmdHelper.get();
                System.out.println("Path to nir: " + nircmdPath);
                new ProcessBuilder(nircmdPath, "changesysvolume", "6500").start();
            }
        }
        catch (Exception e) {
            //
        }
    }

    public static void doOperatingSystemVolumeDown() {
        try {
            if (SystemInfo.isWindows()) {
                String nircmdPath = NirCmdHelper.get();
                System.out.println("Path to nir: " + nircmdPath);
                new ProcessBuilder(nircmdPath, "changesysvolume", "-6500").start();
            }
        }
        catch (Exception e) {
            //
        }
    }

    public static void doAltF4() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (Exception e) {
            //
        }
    }

    public static void doLaunchCommandPrompt() {
        try {
            if (SystemInfo.isWindows()) {
                Runtime.getRuntime().exec("cmd.exe /c start cmd.exe", null, new File(SystemInfo.get(SystemInfo.SYSTEM_PROPERTY_HOME_DIR)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Utility methods needed to perform above requested tasks
    //-------------------------------------------------------------------------

    private static Point getPointerLocation(double ratio_x, double ratio_y) {
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        int x = (int) (rect.width * ratio_x);
        int y = (int) (rect.height * ratio_y);

        return new Point(x, y);
    }

    private static int keyboardKeyToCode(PacketEncoding.KeyboardKeys key) {
        switch (key) {
            case Alt:
                return KeyEvent.VK_ALT;

            case Ampersand:
                return KeyEvent.VK_AMPERSAND;

            case Apostrophe:
                return KeyEvent.VK_QUOTE;

            case At:
                return KeyEvent.VK_AT;

            case BackSlash:
                return KeyEvent.VK_BACK_SLASH;

            case Backspace:
                return KeyEvent.VK_BACK_SPACE;

            case CapsLock:
                return KeyEvent.VK_CAPS_LOCK;

            case CloseBracket:
                return KeyEvent.VK_BRACERIGHT;

            case CloseParenthesis:
                return KeyEvent.VK_RIGHT_PARENTHESIS;

            case CloseSquareBracket:
                return KeyEvent.VK_CLOSE_BRACKET;

            case Colon:
                return KeyEvent.VK_COLON;

            case Comma:
                return KeyEvent.VK_COMMA;

            case Ctrl:
                return KeyEvent.VK_CONTROL;

            case Delete:
                return KeyEvent.VK_DELETE;

            case DollarSign:
                return KeyEvent.VK_DOLLAR;

            case Enter:
                return KeyEvent.VK_ENTER;

            case Escape:
                return KeyEvent.VK_ESCAPE;

            case Equals:
                return KeyEvent.VK_EQUALS;

            case ExclamationMark:
                return KeyEvent.VK_EXCLAMATION_MARK;

            case ForwardSlash:
                return KeyEvent.VK_SLASH;

            case GreaterThan:
                return KeyEvent.VK_GREATER;

            case LessThan:
                return KeyEvent.VK_LESS;

            case Minus:
                return KeyEvent.VK_MINUS;

            case None:
                return -1;

            case OpenBracket:
                return KeyEvent.VK_BRACELEFT;

            case OpenParenthesis:
                return KeyEvent.VK_LEFT_PARENTHESIS;

            case OpenSquareBracket:
                return KeyEvent.VK_OPEN_BRACKET;

            case Opts:
                return KeyEvent.VK_CONTEXT_MENU;

            case Percent:
                return -1;

            case Period:
                return KeyEvent.VK_PERIOD;

            case Plus:
                return KeyEvent.VK_PLUS;

            case Pound:
                return KeyEvent.VK_NUMBER_SIGN;

            case QuestionMark:
                return -1;

            case Quote:
                return KeyEvent.VK_QUOTEDBL;

            case SemiColon:
                return KeyEvent.VK_SEMICOLON;

            case Shift:
                return KeyEvent.VK_SHIFT;

            case Space:
                return KeyEvent.VK_SPACE;

            case Star:
                return KeyEvent.VK_ASTERISK;

            case Super:
                return KeyEvent.VK_WINDOWS;

            case Tab:
                return KeyEvent.VK_TAB;

            case Underscore:
                return KeyEvent.VK_UNDERSCORE;

            case UpArrow:
                return KeyEvent.VK_CIRCUMFLEX;

            case VerticalLine:
                return -1;

            default:
                return -1;
        }
    }

    private static String getScreenCaptureData() {
        try {
            BufferedImage capture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(capture, "jpeg", baos);

            return Base64.encodeBase64String(baos.toByteArray(), false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
