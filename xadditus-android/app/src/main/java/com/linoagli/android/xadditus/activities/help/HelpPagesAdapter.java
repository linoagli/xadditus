/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.Help
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 4/1/17 10:29 AM
 */
package com.linoagli.android.xadditus.activities.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;

public class HelpPagesAdapter extends FragmentStatePagerAdapter {
    private String[] helpTexts = {
        "<strong>Download the PC client.</strong><p>The app is designed to work with it's desktop client counterpart. I currently support Windows 10 and Linux (tested with Ubuntu) operating systems. Please click on the \"Download PC Client\" option in the menu above to download and install the app. Please make sure to read the README.txt file if you are a Linux user.</p>",
        "<h3>Connecting to PC via WIFI</h3><p>The app is set to scan for devices on your WIFI by default. The scan will only list devices that are connected to the same network as your phone and that have the PC client running. You can interupt the scan at any time if you already see the device you seek by touching the screen. The WIFI connection type is the most stable as of now and is recommended.</p>",
        "<h3>Connecting to PC via Bluetooth</h3><p>The app also supports a bluetooth connection protocol (it is still in development and in an experimental phase). To access it, in the device scan screen, click on the menu icon in the top right corner of the screen and select \"Switch to bluetooth\". The scanner will then scan for bluetooth devices. You can switch back to wifi by going through the same step and selecting \"Switch to WIFI\".</p><p>It's important to note that as of now, Android's bluetooth service retreival is iffy at best. So the app will greedily display all bluetooth devices it's able to find. So please keep in mind the bluetooth name of your PC and make sure the app is running on the PC as well when connecting to it via bluetooth. Also note that it may take more than 1 try to find the device you are looking for.</p><p>A sure way to find your bluetooth device is to pair it to your phone before using this app. The app will display all paired devices first always and other scanned devices next. This will eliminate the scanning hassle.</p>"
    };

    public HelpPagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        HelpPageFragment helpPageFragment = new HelpPageFragment();
        helpPageFragment.setContent(Html.fromHtml(helpTexts[position]).toString());

        return helpPageFragment;
    }

    @Override
    public int getCount() {
        return helpTexts.length;
    }
}