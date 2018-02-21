/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 9/17/15 5:22 PM
 */
package com.linoagli.android.xadditus.activities.input.fragments;

import android.media.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.linoagli.android.xadditus.R;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.Models.MixerDetails;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MicrophoneFragment extends Fragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Spinner sp_outputList;
    ToggleButton tb_micSwitch;
    Button bt_getOutputList;

    Handler handler = new Handler();
    AudioCaptureThread audioCaptureThread;
    AudioTrack audioTrack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        sp_outputList = (Spinner) view.findViewById(R.id.sp_microphone_outputList);
        tb_micSwitch = (ToggleButton) view.findViewById(R.id.tb_microphone_micSwitch);
        bt_getOutputList = (Button) view.findViewById(R.id.bt_microphone_getOutputList);


        int bufferSize = AudioTrack.getMinBufferSize(Constants.AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Constants.AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();

        sp_outputList.setOnItemSelectedListener(this);
        tb_micSwitch.setOnCheckedChangeListener(this);
        bt_getOutputList.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        requestAudioOutputList();
    }

    @Override
    public void onStop() {
        audioTrack.pause();
        audioTrack.flush();
        audioTrack.stop();
        audioTrack = null;

        stopAudioCapture();

        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        StringBuilder sb = new StringBuilder();
        sb.append(PacketEncoding.PacketType.SetAudioOutPut.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
        sb.append(position).append(Constants.STRING_TOKENIZER_DELIMITOR);

//        App.get(getActivity()).getConnectionsManager().postRequest(sb.toString()); TODO re-enable this
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            startAudioCapture();
        } else {
            stopAudioCapture();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bt_getOutputList) requestAudioOutputList();
    }

    public void handleMessage(String data) {
        StringTokenizer st = new StringTokenizer(data, Constants.STRING_TOKENIZER_DELIMITOR);
        String packetTypeString = st.nextToken();
        PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

        switch (packetType) {
            case AudioOutputList: {
                String mixerDetailsData = st.nextToken();
                String currMixerIndexString = st.nextToken();

                try {
                    byte bytes[] = Base64.decodeBase64(mixerDetailsData);
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    List<MixerDetails> mixerDetails = (ArrayList<MixerDetails>) ois.readObject();
                    setMixerDetails(mixerDetails);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int currMixerIndex = 0;
                currMixerIndex = Integer.parseInt(currMixerIndexString);

                setCurrMixerIndex(currMixerIndex);
                break;
            }

            case AudioData: {
                System.out.println("audio data received...");

                String audioData = st.nextToken();

                System.out.println(audioData.length());
                byte[] bytes = Base64.decodeBase64(audioData);

                if (audioTrack != null) {
                    System.out.println("writing audio data: " + bytes.length);
                    audioTrack.write(bytes, 0, bytes.length);
                }
                break;
            }
        }
    }

    public void setMixerDetails(List<MixerDetails> mixerDetails) {
        String[] mixers = new String[mixerDetails.size()];
        for (int ii = 0; ii < mixers.length; ii++) {
            mixers[ii] = mixerDetails.get(ii).name + " - " + mixerDetails.get(ii).vendor;
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mixers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        handler.post(new Runnable() {
            @Override
            public void run() {
                sp_outputList.setAdapter(adapter);
            }
        });
    }

    public void setCurrMixerIndex(final int index) {
        if (index == -1 || sp_outputList.getAdapter() == null || sp_outputList.getAdapter().getCount() <= index) return;

        handler.post(new Runnable() {
            @Override
            public void run() {
                sp_outputList.setOnItemSelectedListener(null);
                sp_outputList.setSelection(index);
                sp_outputList.setOnItemSelectedListener(MicrophoneFragment.this);
            }
        });
    }

    private void requestAudioOutputList() {
        StringBuilder sb = new StringBuilder();
        sb.append(PacketEncoding.PacketType.AudioOutputList.toString());
        sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

//        App.get(getActivity()).getConnectionsManager().postRequest(sb.toString()); TODO re-enable this
    }

    private void startAudioCapture() {
        System.out.println("Starting audio capture.");

        audioCaptureThread = new AudioCaptureThread();
        audioCaptureThread.start();
    }

    private void stopAudioCapture() {
        System.out.println("Stopping audio capture.");

        if (audioCaptureThread != null) {
            audioCaptureThread.cancel();
            audioCaptureThread = null;
        }
    }

    private class AudioCaptureThread extends Thread {
        private final int SAMPLE_RATE = 44100;
        private final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
        private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

        private AudioRecord audioRecord;

        private boolean runLoop = true;
        private byte[] buffer;
        private int bufferSize;

        @Override
        public void run() {
            try {
                bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
                buffer = new byte[bufferSize];

                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
                audioRecord.startRecording();

                System.out.println("Audio capture started...");

                while (runLoop) {
                    audioRecord.read(buffer, 0, buffer.length);

                    StringBuilder sb = new StringBuilder();
                    sb.append(PacketEncoding.PacketType.AudioData.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
                    sb.append(Base64.encodeBase64String(buffer)).append(Constants.STRING_TOKENIZER_DELIMITOR);

//                    App.get(getActivity()).getConnectionsManager().postRequest(sb.toString()); TODO re-enable this

                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        public void cancel() {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }

            runLoop = false;
        }
    }
}
