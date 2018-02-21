/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by linoagli on 2/5/15.
 * Xadditus Core Project.
 *
 */
package com.linoagli.java.XadditusCore;

public class PacketEncoding
{
    public enum PacketType
    {
        Scan, ScanResponse,
        DataSync,
        KeyDown, KeyUp, AsciiKeyDown, AsciiKeyUp,
        MouseMove, MouseScroll, MouseLeftDown, MouseLeftUp, MouseLeftClick, MouseRightDown, MouseRightUp, MouseRightClick,
        AudioData, AudioOutputList, AudioInputList, SetAudioOutPut,
        RemoteDesktopScreenCapture, RemoteDesktopTouchDown, RemoteDesktopTouchUp, RemoteDesktopTouchMove,
        TerminalCommand, TerminalVerboseOutput, TerminalErrorOutput,
        OperatingSystemVolumeUp, OperatingSystemVolumeDown, AltF4, LaunchTerminal
    }

    public enum KeyboardKeys
    {
        None,
        Escape, Backspace, Tab, Delete, CapsLock, Enter, Shift, Ctrl, Super, Alt, Opts, Space,
        At, Pound, DollarSign, Percent, UpArrow, Ampersand, Star, OpenParenthesis, CloseParenthesis, ExclamationMark,
        Underscore, OpenBracket, CloseBracket, OpenSquareBracket, CloseSquareBracket, ForwardSlash, BackSlash,
        Plus, Minus, Equals, Apostrophe, Quote, Colon, SemiColon,
        VerticalLine, LessThan, GreaterThan, QuestionMark, Comma, Period
    }
}
