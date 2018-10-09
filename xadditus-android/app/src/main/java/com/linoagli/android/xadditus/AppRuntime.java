/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus;

import com.linoagli.android.xadditus.processes.ConnectionsManager;
import com.linoagli.android.xadditus.processes.DeviceScanner;

public class AppRuntime {
    public static boolean isAppInForeground = false;
    public static DeviceScanner deviceScanner = new DeviceScanner();
    public static ConnectionsManager connectionsManager = new ConnectionsManager();
}