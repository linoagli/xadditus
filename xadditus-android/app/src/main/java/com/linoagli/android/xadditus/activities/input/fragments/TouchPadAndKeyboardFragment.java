/**
 * Xadditus Android Project.
 *
 * @author Olubusayo K. Faye-Lino Agli on user linoagli
 * @since 6/29/15
 */
package com.linoagli.android.xadditus.activities.input.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.linoagli.android.xadditus.R;

public class TouchPadAndKeyboardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_touchpad_and_keyboard, container, false);
    }
}
