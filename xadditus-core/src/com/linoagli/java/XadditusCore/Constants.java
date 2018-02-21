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

public class Constants
{
    public static final String DIR_DATA = "/xadditus/data/";

    public static final int SOCKET_PORT_UDP = 14521;
    public static final int SOCKET_PORT_TCP_MAIN = 14522;
    public static final int SOCKET_PORT_TCP_HEAVY_TRAFFIC = 14523;

    public static final int AUDIO_SAMPLE_RATE = 44100;
    public static final int AUDIO_SAMPLE_SIZE = 16;
    public static final int AUDIO_CHANNEL_COUNT = 2;

    public static final String BLUETOOTH_SERVICE_UUID = "7c4bb05c-a74c-4bc2-bb4c-c66348e53440";
    public static final String BLUETOOTH_SERVICE_UUID_AS_SEEN_BY_ANDROID = "4034e548-63c6-4cbb-c24b-4ca75cb04b7c";

    public static final String STRING_TOKENIZER_DELIMITOR = "|||";
    public static final String STRING_NEW_LINE_TOKEN = "<newline />";
}
