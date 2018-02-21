/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}