/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.helpers
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.helpers

import java.util.regex.Pattern

object SystemInfo {
    val SYSTEM_PROPERTY_OS_NAME = "os.name"
    val SYSTEM_PROPERTY_OS_VERSION = "os.version"
    val SYSTEM_PROPERTY_HOME_DIR = "user.home"
    val SYSTEM_PROPERTY_CURRENT_WORKING_DIR = "user.dir"

    enum class OperatingSystem {
        Windows, Linux, SunOS, FreeBSD, Mac, None
    }

    operator fun get(systemProperty: String): String {
        return System.getProperty(systemProperty)
    }

    fun getOperatingSystemFromName(osName: String): OperatingSystem {
        var osName = osName
        osName = osName.toLowerCase()

        for (os in OperatingSystem.values()) {
            val pattern = Pattern.compile(os.name.toLowerCase())
            val found = pattern.matcher(osName).find()

            if (found) return os
        }

        return OperatingSystem.None
    }

    fun isLinux(): Boolean {
        val osName = get(SYSTEM_PROPERTY_OS_NAME)
        val os = getOperatingSystemFromName(osName)
        return os == OperatingSystem.Linux
    }

    fun isWindows(): Boolean {
        val osName = get(SYSTEM_PROPERTY_OS_NAME)
        val os = getOperatingSystemFromName(osName)
        return os == OperatingSystem.Windows
    }
}