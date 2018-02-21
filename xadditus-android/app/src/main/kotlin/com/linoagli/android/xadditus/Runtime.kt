/**
 * Xadditus Android Project.
 * com.linoagli.android.xadditus
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus

import com.linoagli.android.xadditus.helpers.ConnectionsManager
import com.linoagli.android.xadditus.helpers.DeviceScanner

object Runtime {
    var isAppInForeground = false
    val deviceScanner = DeviceScanner()
    val connectionsManager = ConnectionsManager()
}