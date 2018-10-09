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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.linoagli.android.xadditus.*;
import com.linoagli.android.xadditus.adapters.DeviceScanListAdapter;
import com.linoagli.android.xadditus.base.BaseActivity;
import com.linoagli.android.xadditus.services.ConnectionService;
import com.linoagli.android.xadditus.utils.NetworkUtils;
import com.linoagli.android.xadditus.utils.SharedPrefsHelper;
import com.linoagli.java.XadditusCore.Models.Device;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class DeviceScanActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_FINE_LOCATION_PERMISSION = 0x341;

    private ProgressBar pb_progress;
    private RecyclerView rv_scanDevices;
    private FloatingActionButton fab_refresh;

    private DeviceScanListAdapter deviceScanListAdapter;

    private Disposable scanSubscriber;
    private Disposable connectionSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Binding variables to layout views
        pb_progress = (ProgressBar) findViewById(R.id.pb_scan_progress);
        rv_scanDevices = (RecyclerView) findViewById(R.id.rv_scan_devices);
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab_scan_refresh);

        // Setting up the devices recycler view
        deviceScanListAdapter = new DeviceScanListAdapter();
        rv_scanDevices.setHasFixedSize(true);
        rv_scanDevices.setLayoutManager(new LinearLayoutManager(this));
        rv_scanDevices.setAdapter(deviceScanListAdapter);

        // Showing the help activity for the first time
        showFirstTimeHelp();

        // Setting up listeners
        fab_refresh.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposeOfSubscriber(scanSubscriber);
        disposeOfSubscriber(connectionSubscriber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mi_scan_help) startActivity(new Intent(this, HelpActivity.class));

        if (item.getItemId() == R.id.mi_scan_downloadPCClient) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=0B-BkZ-uBQsZMWVhuTEtxcXAtcms"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_FINE_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doScan();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab_refresh) doScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceScanStartedEvent event) {
        deviceScanListAdapter.removeAllDevices();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceFoundEvent event) {
        deviceScanListAdapter.addDevice(event.getDevice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceSelectedEvent event) {
        connectToDevice(event.getDevice());
    }

    private void showFirstTimeHelp() {
        boolean isFirstTimeLoad = SharedPrefsHelper.load(this, Constants.PREFS_FIRST_TIME_LAUNCH, true);

        if (isFirstTimeLoad) {
            startActivity(new Intent(this, HelpActivity.class));
            SharedPrefsHelper.save(this, Constants.PREFS_FIRST_TIME_LAUNCH, false);
        }
    }

    private void doScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION_PERMISSION);
            return;
        }

        if (!NetworkUtils.isConnectedToWifi(this)) {
            Toast.makeText(this, getString(R.string.string_wifiOff), Toast.LENGTH_SHORT).show();
        }

        if (!NetworkUtils.isConnectedToBluetooth()) {
            Toast.makeText(this, getString(R.string.string_bluetoothOff), Toast.LENGTH_SHORT).show();
        }

        Completable completable = Completable.create(
            e -> {
                System.out.println("Initiating scan...");
                AppRuntime.deviceScanner.startScan(DeviceScanActivity.this);
                e.onComplete();
            })
            .delay(20, TimeUnit.SECONDS)
            .andThen(Completable.create(
                e -> {
                    System.out.println("Finishing scan...");
                    AppRuntime.deviceScanner.stopScan();
                    e.onComplete();
                })
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> pb_progress.setVisibility(View.VISIBLE))
            .doFinally(() -> pb_progress.setVisibility(View.INVISIBLE));

        disposeOfSubscriber(scanSubscriber);
        scanSubscriber = completable.subscribe(
            () -> System.out.println("Scan complete!"),
            throwable -> System.out.println("Something bad happened: " + throwable.getMessage())
        );
    }

    private void connectToDevice(Device device) {
        Completable completable = Completable.create(
            e -> {
                AppRuntime.deviceScanner.stopScan();

                boolean didStartService = AppRuntime.connectionsManager.startService(device);

                if (didStartService) {
                    SharedPrefsHelper.save(DeviceScanActivity.this, Constants.PREFS_LAST_SELECTED_DEVICE, device.name);
                    e.onComplete();
                } else {
                    e.onError(new Exception("Failed to start connection service..."));
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> pb_progress.setVisibility(View.VISIBLE))
            .doFinally(() -> pb_progress.setVisibility(View.INVISIBLE));

        disposeOfSubscriber(connectionSubscriber);
        connectionSubscriber = completable.subscribe(
            () -> {
                startService(new Intent(DeviceScanActivity.this, ConnectionService.class));
                startActivity(new Intent(DeviceScanActivity.this, InputInterfaceActivity.class));
            },
            throwable -> System.out.println("Something bad happened: " + throwable.getMessage())
        );
    }

    private void disposeOfSubscriber(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
