/**
 * Xadditus Core Project.
 * com.linoagli.java.XadditusCore.Models
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 9/22/15 5:41 PM
 */
package com.linoagli.java.XadditusCore.Models;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.io.Serializable;

public class MixerDetails implements Serializable
{
    private static final long serialVersionUID = 4617908635473416731L;

    public String name;
    public String vendor;
    public String description;

    public MixerDetails(String name, String vendor, String description)
    {
        this.name = name;
        this.vendor = vendor;
        this.description = description;
    }

    public Mixer getMixer()
    {
        for (Mixer.Info info : AudioSystem.getMixerInfo())
        {
            if (info.getName().equals(name) && info.getVendor().equals(vendor) && info.getDescription().equals(description))
                return AudioSystem.getMixer(info);
        }

        return null;
    }
}
