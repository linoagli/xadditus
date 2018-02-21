/**
 * xadditus App Project.
 * com.linoagli.java.xadditus
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 2/8/16 12:58 AM
 */
package com.linoagli.java.xadditus;

import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.MixerDetails;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class AudioPlaybackManager
{
    private AudioFormat audioFormat;
    private DataLine.Info sourceDataLineInfo;
    private List<MixerDetails> mixerDetails;
    private SourceDataLine sourceDataLine;

    private int currMixerIndex = -1;
    
    public void init()
    {
        audioFormat = new AudioFormat(Constants.AUDIO_SAMPLE_RATE, Constants.AUDIO_SAMPLE_SIZE, Constants.AUDIO_CHANNEL_COUNT, true, false);
        sourceDataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        mixerDetails = new ArrayList<>();
        
        for (Mixer.Info item : AudioSystem.getMixerInfo())
        {
            Mixer mixer = AudioSystem.getMixer(item);
            if (mixer.isLineSupported(sourceDataLineInfo)) mixerDetails.add(new MixerDetails(item.getName(), item.getVendor(), item.getDescription()));
        }

        System.out.println("Found " + mixerDetails.size() + " source supporting mixers");
        System.out.println("Initializing the audio source data line...");

        if (mixerDetails.size() > 0) currMixerIndex = 0;

        try
        {
            Mixer mixer = mixerDetails.get(currMixerIndex).getMixer();

            System.out.println("selected output audio mixer: " + mixerDetails.get(currMixerIndex).name);
            
            sourceDataLine = (SourceDataLine) mixer.getLine(sourceDataLineInfo);
            sourceDataLine.open();
            sourceDataLine.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<MixerDetails> getMixerDetails()
    {
        return mixerDetails;
    }

    public int getCurrMixerIndex()
    {
        return currMixerIndex;
    }
    
    public void write(byte[] bytes)
    {
        if (sourceDataLine != null) sourceDataLine.write(bytes, 0, bytes.length);
    }
    
    public void setCurrMixerIndex(int index)
    {
        if (mixerDetails.size() > index) currMixerIndex = index;

        Mixer mixer = mixerDetails.get(currMixerIndex).getMixer();

        System.out.println("Changing audio output mixer to: " + mixerDetails.get(currMixerIndex).name);
        
        if (sourceDataLine != null)
        {
            sourceDataLine.flush();
            sourceDataLine.stop();
            sourceDataLine.close();
            sourceDataLine = null;
        }

        try
        {
            sourceDataLine = (SourceDataLine) mixer.getLine(sourceDataLineInfo);
            sourceDataLine.open();
            sourceDataLine.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void cleanUp()
    {
        audioFormat = null;
        sourceDataLineInfo = null;
        
        mixerDetails.clear();
        mixerDetails = null;

        if (sourceDataLine != null)
        {
            sourceDataLine.flush();
            sourceDataLine.stop();
            sourceDataLine.close();
            sourceDataLine = null;
        }
    }
}
