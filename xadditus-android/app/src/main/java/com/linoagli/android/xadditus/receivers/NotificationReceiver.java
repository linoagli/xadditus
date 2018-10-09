/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.receivers
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.linoagli.android.xadditus.Constants;
import com.linoagli.android.xadditus.SendRequestEvent;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.INTENT_ACTION_REQUEST_INCREASE_SYSTEM_VOLUME)) {
            String request = new StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeUp.toString())
                .append(com.linoagli.java.XadditusCore.Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();

            EventBus.getDefault().post(new SendRequestEvent(request, false));
        }

        if (intent.getAction().equals(Constants.INTENT_ACTION_REQUEST_DECREASE_SYSTEM_VOLUME)) {
            String request = new StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeDown.toString())
                .append(com.linoagli.java.XadditusCore.Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();

            EventBus.getDefault().post(new SendRequestEvent(request, false));
        }
    }
}