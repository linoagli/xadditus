/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.fragments
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.SendRequestEvent;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;

public class SystemControlFragment extends Fragment implements View.OnClickListener {
    private Button bt_altF4;
    private Button bt_volumeUp;
    private Button bt_volumeDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_control, container, false);

        bt_altF4 = view.findViewById(R.id.bt_systemControl_altF4);
        bt_volumeUp = view.findViewById(R.id.bt_systemControl_volumeUp);
        bt_volumeDown = view.findViewById(R.id.bt_systemControl_volumeDown);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_altF4.setOnClickListener(this);
        bt_volumeUp.setOnClickListener(this);
        bt_volumeDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_altF4) {
            String request = new StringBuilder()
                .append(PacketEncoding.PacketType.AltF4.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();

            EventBus.getDefault().post(new SendRequestEvent(request, true));
        }

        if (v == bt_volumeUp) {
            String request = new StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeUp.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();

            EventBus.getDefault().post(new SendRequestEvent(request, false));
        }

        if (v == bt_volumeDown) {
            String request = new StringBuilder()
                .append(PacketEncoding.PacketType.OperatingSystemVolumeDown.toString())
                .append(Constants.STRING_TOKENIZER_DELIMITOR)
                .toString();

            EventBus.getDefault().post(new SendRequestEvent(request, false));
        }
    }
}