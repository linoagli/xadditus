/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus.utils
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.utils;

import java.util.regex.Pattern;

public class SystemInfo {
    public static final String SYSTEM_PROPERTY_OS_NAME = "os.name";
    public static final String SYSTEM_PROPERTY_OS_VERSION = "os.version";
    public static final String SYSTEM_PROPERTY_HOME_DIR = "user.home";
    public static final String SYSTEM_PROPERTY_CURRENT_WORKING_DIR = "user.dir";

    public enum OperatingSystem { Windows, Linux, SunOS, FreeBSD, Mac, None }

    public static String get(String systemProperty)
    {
        return System.getProperty(systemProperty);
    }

    public static OperatingSystem getOperatingSystemFromName(String osName)
    {
        osName = osName.toLowerCase();

        for (OperatingSystem os : OperatingSystem.values())
        {
            Pattern pattern = Pattern.compile(os.name().toLowerCase());
            boolean found = pattern.matcher(osName).find();

            if (found) return os;
        }

        return OperatingSystem.None;
    }

    public static boolean isLinux()
    {
        String osName = get(SYSTEM_PROPERTY_OS_NAME);
        OperatingSystem os = getOperatingSystemFromName(osName);

        return os == OperatingSystem.Linux;
    }

    public static boolean isWindows()
    {
        String osName = get(SYSTEM_PROPERTY_OS_NAME);
        OperatingSystem os = getOperatingSystemFromName(osName);

        return os == OperatingSystem.Windows;
    }
}