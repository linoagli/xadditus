/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.helpers
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.helpers

import com.linoagli.java.XadditusCore.Constants
import com.linoagli.java.XadditusCore.PacketEncoding

object RequestFactory {
    fun createDataSyncRequest(): String {
        return StringBuilder()
                .append(PacketEncoding.PacketType.DataSync.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString()
    }

    fun createSystemVolumeUpRequest(): String {
        return StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeUp.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString()
    }

    fun createSystemVolumeDownRequest(): String {
        return StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeDown.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString()
    }
}