/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/4/17 5:16 PM
 */
package com.linoagli.android.xadditus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.linoagli.android.xadditus.fragments.OperatingSystemControlFragment;
import com.linoagli.android.xadditus.activities.input.fragments.Terminal.TerminalFragment;
import com.linoagli.android.xadditus.activities.input.fragments.TouchPadAndKeyboardFragment;

public class InputInterfacePagerAdapter extends FragmentPagerAdapter {
    public static final int FRAGMENT_COUNT = 3;

    public static final int FRAGMENT_TOUCH_PAD_AND_KEYBOARD = 0;
    public static final int FRAGMENT_TERMINAL = 1;
    public static final int FRAGMENT_OS_CONTROL = 2;

    private TouchPadAndKeyboardFragment touchPadAndKeyboardFragment;
    private TerminalFragment terminalFragment;
    private OperatingSystemControlFragment operatingSystemControlFragment;

    public InputInterfacePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_TOUCH_PAD_AND_KEYBOARD: {
                if (touchPadAndKeyboardFragment == null) {
                    touchPadAndKeyboardFragment = new TouchPadAndKeyboardFragment();
                }
                return touchPadAndKeyboardFragment;
            }

            case FRAGMENT_TERMINAL: {
                if (terminalFragment == null) {
                    terminalFragment = new TerminalFragment();
                }
                return terminalFragment;
            }

            case FRAGMENT_OS_CONTROL: {
                if (operatingSystemControlFragment == null) {
                    operatingSystemControlFragment = new OperatingSystemControlFragment();
                }
                return operatingSystemControlFragment;
            }

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
