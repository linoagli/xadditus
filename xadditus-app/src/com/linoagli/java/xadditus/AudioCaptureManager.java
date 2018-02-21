/**
 * xadditus App Project.
 * com.linoagli.java.xadditus
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 2/8/16 12:57 AM
 */
package com.linoagli.java.xadditus;

import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.MixerDetails;
import com.linoagli.java.library.Process.BaseLoopThread;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class AudioCaptureManager
{
    private AudioFormat audioFormat;
    private DataLine.Info targetDataLineInfo;
    private List<MixerDetails> mixerDetails;
    private TargetDataLine targetDataLine;

    private Callback callback;
    private ReadThread readThread;

    private int currMixerIndex = -1;

    public void init()
    {
        audioFormat = new AudioFormat(Constants.AUDIO_SAMPLE_RATE, Constants.AUDIO_SAMPLE_SIZE, Constants.AUDIO_CHANNEL_COUNT, true, false);
        targetDataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        mixerDetails = new ArrayList<>();

        for (Mixer.Info item : AudioSystem.getMixerInfo())
        {
            Mixer mixer = AudioSystem.getMixer(item);

            if (mixer.isLineSupported(targetDataLineInfo))
                mixerDetails.add(new MixerDetails(item.getName(), item.getVendor(), item.getDescription()));
        }

        System.out.println("Found " + mixerDetails.size() + " output supporting mixers");
        System.out.println("Initializing the audio output data line...");

        if (mixerDetails.size() > 0) currMixerIndex = 0;

        try
        {
            Mixer mixer = mixerDetails.get(currMixerIndex).getMixer();

            System.out.println("selected output audio mixer: " + mixerDetails.get(currMixerIndex).name);

            targetDataLine = (TargetDataLine) mixer.getLine(targetDataLineInfo);
            targetDataLine.open();
            targetDataLine.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setCallback(Callback callback)
    {
        this.callback = callback;
    }

    public void start()
    {
        readThread = new ReadThread();
        readThread.setSleepTime(20);
        readThread.start();
    }

    public void stop()
    {
        if (readThread != null)
        {
            readThread.finish();
            readThread = null;
        }
    }

    public void cleanUp()
    {
        stop();

        audioFormat = null;
        targetDataLineInfo = null;

        mixerDetails.clear();
        mixerDetails = null;

        if (targetDataLine != null)
        {
            targetDataLine.flush();
            targetDataLine.stop();
            targetDataLine.close();
            targetDataLine = null;
        }
    }

    private class ReadThread extends BaseLoopThread
    {
        private byte[] buffer;

        @Override
        public void doBeforeLoop()
        {
            System.out.println("Starting target dataline read thread...");
            buffer = new byte[targetDataLine.getBufferSize() / 15];
        }

        @Override
        public void doLoop()
        {
            if (targetDataLine == null || callback == null) return;

            try
            {
                int count = targetDataLine.read(buffer, 0, buffer.length);
                callback.onRead(buffer);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void doAfterLoop() {}

        @Override
        public void cleanUp() {}
    }

    public interface Callback
    {
        public void onRead(byte[] data);
    }
}
