/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 4/19/16 4:46 PM
 */
package com.linoagli.android.xadditus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.linoagli.android.xadditus.R;

public class MusicForwarderFragment extends Fragment implements View.OnClickListener {
    ToggleButton tb_pausePlay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_forwarder, container, false);

        tb_pausePlay = (ToggleButton) view.findViewById(R.id.tb_musicForwarder_pausePlay);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == tb_pausePlay) doPausePlay();
    }

    private void doPausePlay() {

    }
}
