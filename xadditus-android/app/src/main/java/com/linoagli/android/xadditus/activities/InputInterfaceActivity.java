/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by Olubusayo K. Faye-Lino Agli on 6/8/15.
 * Xadditus Android Project.
 */
package com.linoagli.android.xadditus.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.linoagli.android.xadditus.AppRuntime;
import com.linoagli.android.xadditus.ConnectionInactiveDetectedEvent;
import com.linoagli.android.xadditus.Constants;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.adapters.InputInterfacePagerAdapter;
import com.linoagli.android.xadditus.base.BaseActivity;
import com.linoagli.android.xadditus.utils.SharedPrefsHelper;
import com.linoagli.compoundviews.ViewPagerExtended;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class InputInterfaceActivity extends BaseActivity {
    private ViewPagerExtended viewPager;

    private InputInterfacePagerAdapter inputInterfacePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_interface);

        // Setting up the action bar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(AppRuntime.connectionsManager.getDevice().name);
        }

        // Binding variables to layout views
        viewPager = (ViewPagerExtended) findViewById(R.id.fw_inputInterface_fragments);

        // Setting up the view pager
        inputInterfacePagerAdapter = new InputInterfacePagerAdapter(getSupportFragmentManager());
        viewPager.setSwipeGestureEnabled(false);
        viewPager.setAdapter(inputInterfacePagerAdapter);
        viewPager.setOffscreenPageLimit(inputInterfacePagerAdapter.getCount());
        viewPager.setCurrentItem(InputInterfacePagerAdapter.FRAGMENT_TOUCH_PAD_AND_KEYBOARD, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input_interface, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItemKeyboardAndMouse) {
            showFragmentAtIndex(InputInterfacePagerAdapter.FRAGMENT_TOUCH_PAD_AND_KEYBOARD);
        }

        if (item.getItemId() == R.id.menuItemOsControls) {
            showFragmentAtIndex(InputInterfacePagerAdapter.FRAGMENT_OS_CONTROL);
        }

        if (item.getItemId() == R.id.menuItemDisconnect) {
            AppRuntime.connectionsManager.stopService();
            SharedPrefsHelper.save(this, Constants.PREFS_LAST_SELECTED_DEVICE, null);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ConnectionInactiveDetectedEvent event) {
        Toast.makeText(this, getString(R.string.string_connectionInactiveClosingActivity), Toast.LENGTH_LONG).show();
        finish();
    }

    private void showFragmentAtIndex(int index) {
        if (viewPager.getCurrentItem() != index) {
            viewPager.setCurrentItem(index, false);
        }
    }
}
