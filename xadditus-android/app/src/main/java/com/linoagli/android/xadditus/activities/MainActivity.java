/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.activities
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.linoagli.android.xadditus.AppRuntime;
import com.linoagli.android.xadditus.base.BaseActivity;
import com.linoagli.android.xadditus.services.AutoConnectService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, AutoConnectService.class));

        if (AppRuntime.connectionsManager.isServiceActive()) {
            startActivity(new Intent(this, InputInterfaceActivity.class));
        } else {
            startActivity(new Intent(this, DeviceScanActivity.class));
        }

        finish();
    }
}