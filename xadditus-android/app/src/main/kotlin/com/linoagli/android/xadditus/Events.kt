/**
 * Xadditus Android Project.
 * com.linoagli.android.xadditus.events
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus

import com.linoagli.java.XadditusCore.Models.Device

class DeviceScanStartedEvent
class DeviceFoundEvent(val device: Device)
class DeviceSelectedEvent(val device: Device)
class DataReceivedEvent(val data: String)

class SendRequestEvent(val request: String)

class ConnectionInactiveDetectedEvent