/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.base
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.base;

import android.support.v7.app.AppCompatActivity;
import com.linoagli.android.xadditus.AppRuntime;
import org.greenrobot.eventbus.EventBus;

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        AppRuntime.isAppInForeground = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        AppRuntime.isAppInForeground = false;
        super.onPause();
    }
}