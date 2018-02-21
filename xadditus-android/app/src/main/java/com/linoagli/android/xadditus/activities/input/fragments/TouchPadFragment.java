/**
 * Copyright (C) 2015 Olubusayo K. Faye-Lino Agli.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by Olubusayo K. Faye-Lino Agli on 6/9/15.
 * Xadditus Android Project.
 */
package com.linoagli.android.xadditus.activities.input.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.SendRequestEvent;
import com.linoagli.android.xadditus.helpers.SharedPrefsHelper;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;

public class TouchPadFragment extends Fragment {
    private final int TOUCH_PAD_MOVE_DISTANCE_THRESHOLD = 3;
    private final long TOUCH_PAD_MOVE_TIME_THRESHOLD = 700;

    private final int MOUSE_SPEED_MAX = 10;
    private final int MOUSE_SPEED_DEFAULT = 4;

    private final String PREF_MOUSE_SPEED = "pref_mouseSpeed";

    private SeekBar sb_mouseSpeed;
    private View bt_pad;
    private View bt_leftButton;
    private View bt_rightButton;

    private PointF prevTouchPoint = new PointF();
    private long touchDownTime = 0;
    private boolean touchDidMove = false;
    private int mouseSpeed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_touchpad, container, false);

        // Binding variables to layout views
        sb_mouseSpeed = (SeekBar) view.findViewById(R.id.sb_touchPad_mouseSpeed);
        bt_pad = view.findViewById(R.id.bt_touchPad_pad);
        bt_leftButton = view.findViewById(R.id.bt_touchPad_leftMouseButton);
        bt_rightButton = view.findViewById(R.id.bt_touchPad_rightMouseButton);

        mouseSpeed = SharedPrefsHelper.load(getActivity(), PREF_MOUSE_SPEED, MOUSE_SPEED_DEFAULT);
        sb_mouseSpeed.setMax(MOUSE_SPEED_MAX);
        sb_mouseSpeed.setProgress(mouseSpeed);

        // Setting up the listeners
        setupListeners();

        return view;
    }

    private void setupListeners() {
        sb_mouseSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mouseSpeed = progress + 1;
                SharedPrefsHelper.save(getActivity(), PREF_MOUSE_SPEED, mouseSpeed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        bt_pad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Initializing all the event related variables
                    prevTouchPoint.set(event.getX(), event.getY());
                    touchDownTime = System.currentTimeMillis();
                    touchDidMove = false;
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float dx = event.getX() - prevTouchPoint.x;
                    float dy = event.getY() - prevTouchPoint.y;

                    StringBuilder sb = new StringBuilder();

                    if (event.getPointerCount() > 1) {
                        sb.append(PacketEncoding.PacketType.MouseScroll.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
                        sb.append(dy / 8 * mouseSpeed).append(Constants.STRING_TOKENIZER_DELIMITOR);

                        EventBus.getDefault().post(new SendRequestEvent(sb.toString()));
                    } else {
                        sb.append(PacketEncoding.PacketType.MouseMove.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
                        sb.append(dx / 2 * mouseSpeed).append(Constants.STRING_TOKENIZER_DELIMITOR);
                        sb.append(dy / 2 * mouseSpeed).append(Constants.STRING_TOKENIZER_DELIMITOR);
                        sb.append(false).append(Constants.STRING_TOKENIZER_DELIMITOR);

                        EventBus.getDefault().post(new SendRequestEvent(sb.toString()));
                    }

                    prevTouchPoint.set(event.getX(), event.getY());
                    touchDidMove = Math.abs(dx) >= TOUCH_PAD_MOVE_DISTANCE_THRESHOLD || Math.abs(dy) >= TOUCH_PAD_MOVE_DISTANCE_THRESHOLD;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // If we didn't move the mouse and we are within the time constraints, then simulate a left mouse click
                    long timeElapsed = System.currentTimeMillis() - touchDownTime;
                    boolean canClick = TOUCH_PAD_MOVE_TIME_THRESHOLD > timeElapsed;

                    if (!touchDidMove && canClick) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(PacketEncoding.PacketType.MouseLeftClick.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);

                        EventBus.getDefault().post(new SendRequestEvent(sb.toString()));
                    }

                    // Clearing all the event related variables (just for my sanity)
                    prevTouchPoint.set(0, 0);
                    touchDownTime = 0;
                    touchDidMove = false;
                }

                return false;
            }
        });

        bt_leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String packetType = null;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    packetType = PacketEncoding.PacketType.MouseLeftDown.toString();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    packetType = PacketEncoding.PacketType.MouseLeftUp.toString();
                }

                if (packetType == null) return false;

                StringBuilder sb = new StringBuilder();
                sb.append(packetType).append(Constants.STRING_TOKENIZER_DELIMITOR);

                EventBus.getDefault().post(new SendRequestEvent(sb.toString()));

                return false;
            }
        });

        bt_rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String packetType = null;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    packetType = PacketEncoding.PacketType.MouseRightDown.toString();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    packetType = PacketEncoding.PacketType.MouseRightUp.toString();
                }

                if (packetType == null) return false;

                StringBuilder sb = new StringBuilder();
                sb.append(packetType).append(Constants.STRING_TOKENIZER_DELIMITOR);

                EventBus.getDefault().post(new SendRequestEvent(sb.toString()));

                return false;
            }
        });
    }
}
