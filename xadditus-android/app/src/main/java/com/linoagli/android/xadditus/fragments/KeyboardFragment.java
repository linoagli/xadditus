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
package com.linoagli.android.xadditus.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.linoagli.android.xadditus.DataReceivedEvent;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.SendRequestEvent;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.StringTokenizer;

public class KeyboardFragment extends Fragment {
    private final String[][] KEYS_NUMBER = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")"}
    };

    private final String[][] KEYS_ALPHA_PRIMARY = {
        {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"},
        {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
    };

    private final String[][] KEYS_ALPHA_SECONDARY = {
        {"", ",", "", "", "", "", "", "", "=", "", ";", "'", "/", ".", "[", "]", "", "", "", "", "-", "", "", "", "", ""},
        {"", "<", "", "", "", "", "", "", "+", "", ":", "\"", "?", ">", "{", "}", "", "", "", "", "_", "", "", "", "", ""}
    };

    private final String KEY_CTRL = "Ctrl";
    private final String KEY_ESCAPE = "Esc";
    private final String KEY_ALT = "Alt";
    private final String KEY_TAB = "Tab";
    private final String KEY_FUNCTION = "Fn";

    private TextView tv_feedback;

    private View vw_special, vw_enter, vw_delete, vw_back, vw_space, vw_shiftLeft, vw_shiftRight, vw_ctrl, vw_escape,
        vw_tab, vw_alt, vw_func;

    private View vw_n1, vw_n2, vw_n3, vw_n4, vw_n5, vw_n6, vw_n7, vw_n8, vw_n9, vw_n0;

    private View vw_aa, vw_ab, vw_ac, vw_ad, vw_ae, vw_af, vw_ag, vw_ah, vw_ai, vw_aj, vw_ak, vw_al, vw_am, vw_an,
        vw_ao, vw_ap, vw_aq, vw_ar, vw_as, vw_at, vw_au, vw_av, vw_aw, vw_ax, vw_ay, vw_az;

    private View[] alphaKeys;
    private View[] numericKeys;

    private Handler handler;

    private boolean isFuncActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);

        tv_feedback = view.findViewById(R.id.tv_keyboard_feedback);

        vw_special = view.findViewById(R.id.partial_keyboard_special);
        vw_enter = view.findViewById(R.id.partial_keyboard_enter);
        vw_delete = view.findViewById(R.id.partial_keyboard_del);
        vw_back = view.findViewById(R.id.partial_keyboard_back);
        vw_space = view.findViewById(R.id.partial_keyboard_space);
        vw_shiftLeft = view.findViewById(R.id.partial_keyboard_shiftLeft);
        vw_shiftRight = view.findViewById(R.id.partial_keyboard_shiftRight);
        vw_ctrl = view.findViewById(R.id.partial_keyboard_ctrl);
        vw_escape = view.findViewById(R.id.partial_keyboard_escape);
        vw_tab = view.findViewById(R.id.partial_keyboard_tab);
        vw_alt = view.findViewById(R.id.partial_keyboard_alt);
        vw_func = view.findViewById(R.id.partial_keyboard_func);

        vw_n1 = view.findViewById(R.id.partial_keyboard_n1);
        vw_n2 = view.findViewById(R.id.partial_keyboard_n2);
        vw_n3 = view.findViewById(R.id.partial_keyboard_n3);
        vw_n4 = view.findViewById(R.id.partial_keyboard_n4);
        vw_n5 = view.findViewById(R.id.partial_keyboard_n5);
        vw_n6 = view.findViewById(R.id.partial_keyboard_n6);
        vw_n7 = view.findViewById(R.id.partial_keyboard_n7);
        vw_n8 = view.findViewById(R.id.partial_keyboard_n8);
        vw_n9 = view.findViewById(R.id.partial_keyboard_n9);
        vw_n0 = view.findViewById(R.id.partial_keyboard_n0);
        vw_aa = view.findViewById(R.id.partial_keyboard_aa);
        vw_ab = view.findViewById(R.id.partial_keyboard_ab);
        vw_ac = view.findViewById(R.id.partial_keyboard_ac);
        vw_ad = view.findViewById(R.id.partial_keyboard_ad);
        vw_ae = view.findViewById(R.id.partial_keyboard_ae);
        vw_af = view.findViewById(R.id.partial_keyboard_af);
        vw_ag = view.findViewById(R.id.partial_keyboard_ag);
        vw_ah = view.findViewById(R.id.partial_keyboard_ah);
        vw_ai = view.findViewById(R.id.partial_keyboard_ai);
        vw_aj = view.findViewById(R.id.partial_keyboard_aj);
        vw_ak = view.findViewById(R.id.partial_keyboard_ak);
        vw_al = view.findViewById(R.id.partial_keyboard_al);
        vw_am = view.findViewById(R.id.partial_keyboard_am);
        vw_an = view.findViewById(R.id.partial_keyboard_an);
        vw_ao = view.findViewById(R.id.partial_keyboard_ao);
        vw_ap = view.findViewById(R.id.partial_keyboard_ap);
        vw_aq = view.findViewById(R.id.partial_keyboard_aq);
        vw_ar = view.findViewById(R.id.partial_keyboard_ar);
        vw_as = view.findViewById(R.id.partial_keyboard_as);
        vw_at = view.findViewById(R.id.partial_keyboard_at);
        vw_au = view.findViewById(R.id.partial_keyboard_au);
        vw_av = view.findViewById(R.id.partial_keyboard_av);
        vw_aw = view.findViewById(R.id.partial_keyboard_aw);
        vw_ax = view.findViewById(R.id.partial_keyboard_ax);
        vw_ay = view.findViewById(R.id.partial_keyboard_ay);
        vw_az = view.findViewById(R.id.partial_keyboard_az);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler();

        bundleKeys();

        setupSpecialKeys();
        updateAlphaNumericKeys();

        setupSpecialKeysListeners();
        setupAlphaNumericKeysListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataReceivedEvent event) {
        try {
            StringTokenizer st = new StringTokenizer(event.getData(), Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            if (packetType == PacketEncoding.PacketType.KeyDown) {
                String keyboardKeyString = st.nextToken();
                String keyString = st.nextToken();
                updateKeyboardFeedbackDisplay(keyboardKeyString, keyString);
            }

            if (packetType == PacketEncoding.PacketType.AsciiKeyDown) {
                String keyString = st.nextToken();
                updateKeyboardFeedbackDisplay(keyString);
            }
        } catch (Exception e) {
            //
        }
    }

    private void bundleKeys() {
        numericKeys = new View[KEYS_NUMBER[0].length];
        numericKeys[0] = vw_n1;
        numericKeys[1] = vw_n2;
        numericKeys[2] = vw_n3;
        numericKeys[3] = vw_n4;
        numericKeys[4] = vw_n5;
        numericKeys[5] = vw_n6;
        numericKeys[6] = vw_n7;
        numericKeys[7] = vw_n8;
        numericKeys[8] = vw_n9;
        numericKeys[9] = vw_n0;

        alphaKeys = new View[KEYS_ALPHA_PRIMARY[0].length];
        alphaKeys[0] = vw_aa;
        alphaKeys[1] = vw_ab;
        alphaKeys[2] = vw_ac;
        alphaKeys[3] = vw_ad;
        alphaKeys[4] = vw_ae;
        alphaKeys[5] = vw_af;
        alphaKeys[6] = vw_ag;
        alphaKeys[7] = vw_ah;
        alphaKeys[8] = vw_ai;
        alphaKeys[9] = vw_aj;
        alphaKeys[10] = vw_ak;
        alphaKeys[11] = vw_al;
        alphaKeys[12] = vw_am;
        alphaKeys[13] = vw_an;
        alphaKeys[14] = vw_ao;
        alphaKeys[15] = vw_ap;
        alphaKeys[16] = vw_aq;
        alphaKeys[17] = vw_ar;
        alphaKeys[18] = vw_as;
        alphaKeys[19] = vw_at;
        alphaKeys[20] = vw_au;
        alphaKeys[21] = vw_av;
        alphaKeys[22] = vw_aw;
        alphaKeys[23] = vw_ax;
        alphaKeys[24] = vw_ay;
        alphaKeys[25] = vw_az;
    }

    private void setupSpecialKeys() {
        ((ImageView) vw_special.findViewById(R.id.iv_key_icon)).setImageResource(R.mipmap.ic_launcher);
        ((ImageView) vw_enter.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_enter);
        ((ImageView) vw_delete.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_delete);
        ((ImageView) vw_back.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_backspace);
        ((ImageView) vw_space.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_space);
        ((ImageView) vw_shiftLeft.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_shift);
        ((ImageView) vw_shiftRight.findViewById(R.id.iv_key_icon)).setImageResource(R.drawable.ic_shift);
        ((TextView) vw_ctrl.findViewById(R.id.tv_key_keyTextPrimary)).setText(KEY_CTRL);
        ((TextView) vw_escape.findViewById(R.id.tv_key_keyTextPrimary)).setText(KEY_ESCAPE);
        ((TextView) vw_tab.findViewById(R.id.tv_key_keyTextPrimary)).setText(KEY_TAB);
        ((TextView) vw_alt.findViewById(R.id.tv_key_keyTextPrimary)).setText(KEY_ALT);
        ((TextView) vw_func.findViewById(R.id.tv_key_keyTextPrimary)).setText(KEY_FUNCTION);
    }

    private void updateAlphaNumericKeys() {
        for (int ii = 0; ii < numericKeys.length; ii++) {
            ((TextView) numericKeys[ii].findViewById(R.id.tv_key_keyTextPrimary)).setText(KEYS_NUMBER[0][ii]);
            ((TextView) numericKeys[ii].findViewById(R.id.tv_key_keyTextSecondary)).setText(KEYS_NUMBER[1][ii]);
        }

        if (isFuncActive) {
            for (int ii = 0; ii < alphaKeys.length; ii++) {
                ((TextView) alphaKeys[ii].findViewById(R.id.tv_key_keyTextPrimary)).setText(KEYS_ALPHA_SECONDARY[0][ii]);
                ((TextView) alphaKeys[ii].findViewById(R.id.tv_key_keyTextSecondary)).setText(KEYS_ALPHA_SECONDARY[1][ii]);
            }
        } else {
            for (int ii = 0; ii < alphaKeys.length; ii++) {
                ((TextView) alphaKeys[ii].findViewById(R.id.tv_key_keyTextPrimary)).setText(KEYS_ALPHA_PRIMARY[0][ii]);
                ((TextView) alphaKeys[ii].findViewById(R.id.tv_key_keyTextSecondary)).setText(KEYS_ALPHA_PRIMARY[1][ii]);
            }
        }
    }

    private void setupSpecialKeysListeners() {
        vw_special.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Super.toString());
                return false;
            }
        });

        vw_enter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Enter.toString());
                return false;
            }
        });

        vw_delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Delete.toString());
                return false;
            }
        });

        vw_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Backspace.toString());
                return false;
            }
        });

        vw_space.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Space.toString());
                return false;
            }
        });

        vw_shiftLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Shift.toString());
                return false;
            }
        });

        vw_shiftRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Shift.toString());
                return false;
            }
        });

        vw_ctrl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Ctrl.toString());
                return false;
            }
        });

        vw_escape.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Escape.toString());
                return false;
            }
        });

        vw_tab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Tab.toString());
                return false;
            }
        });

        vw_alt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendKeyEventRequest(event.getAction(), PacketEncoding.KeyboardKeys.Alt.toString());
                return false;
            }
        });

        vw_func.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFuncActive = !isFuncActive;
                updateAlphaNumericKeys();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupAlphaNumericKeysListeners() {
        for (int ii = 0; ii < alphaKeys.length; ii++) {
            final int index = ii;

            alphaKeys[index].setOnTouchListener((view, event) -> {
                if (isFuncActive) {
                    String keyValueString = characterToKeyValue(KEYS_ALPHA_SECONDARY[0][index]);
                    sendKeyEventRequest(event.getAction(), keyValueString);
                } else {
                    sendAsciiKeyEventRequest(event.getAction(), (int) KEYS_ALPHA_PRIMARY[0][index].charAt(0));
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }

                return false;
            });
        }

        for (int ii = 0; ii < numericKeys.length; ii++) {
            final int index = ii;

            numericKeys[index].setOnTouchListener((view, event) -> {
                sendAsciiKeyEventRequest(event.getAction(), (int) KEYS_NUMBER[0][index].charAt(0));

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }

                return false;
            });
        }
    }

    private void updateKeyboardFeedbackDisplay(String keyString) {
        handler.removeCallbacks(clearKeyboardFeedbackRunnable);

        if (keyString != null && !keyString.isEmpty()) {
            String text = tv_feedback.getText() + keyString;
            tv_feedback.setText(text);
        }

        handler.postDelayed(clearKeyboardFeedbackRunnable, 1000 * 10);
    }

    private void updateKeyboardFeedbackDisplay(String keyboardKeyString, String keyString) {
        PacketEncoding.KeyboardKeys keyboardKey = PacketEncoding.KeyboardKeys.valueOf(keyboardKeyString);

        switch (keyboardKey) {
            case Backspace: {
                String text = tv_feedback.getText().toString();
                text = text.substring(0, text.length() - 1);
                tv_feedback.setText(text);

                keyString = null;
                break;
            }

            case Shift: {

                break;
            }
        }

        updateKeyboardFeedbackDisplay(keyString);
    }

    private void sendKeyEventRequest(int eventAction, String keyValue) {
        String packetType = null;

        if (eventAction == MotionEvent.ACTION_DOWN) {
            packetType = PacketEncoding.PacketType.KeyDown.toString();
        } else if (eventAction == MotionEvent.ACTION_UP) packetType = PacketEncoding.PacketType.KeyUp.toString();

        if (packetType == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append(packetType);
        sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
        sb.append(keyValue);
        sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

        EventBus.getDefault().post(new SendRequestEvent(sb.toString(), true));
    }

    private void sendAsciiKeyEventRequest(int eventAction, int keyValue) {
        String packetType = null;

        if (eventAction == MotionEvent.ACTION_DOWN) {
            packetType = PacketEncoding.PacketType.AsciiKeyDown.toString();
        } else if (eventAction == MotionEvent.ACTION_UP) packetType = PacketEncoding.PacketType.AsciiKeyUp.toString();

        if (packetType == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append(packetType);
        sb.append(Constants.STRING_TOKENIZER_DELIMITOR);
        sb.append(keyValue);
        sb.append(Constants.STRING_TOKENIZER_DELIMITOR);

        EventBus.getDefault().post(new SendRequestEvent(sb.toString(), true));
    }

    private String characterToKeyValue(String character) {
        if (character.equals("!")) return PacketEncoding.KeyboardKeys.ExclamationMark.toString();
        if (character.equals("@")) return PacketEncoding.KeyboardKeys.At.toString();
        if (character.equals("#")) return PacketEncoding.KeyboardKeys.Pound.toString();
        if (character.equals("$")) return PacketEncoding.KeyboardKeys.DollarSign.toString();
        if (character.equals("%")) return PacketEncoding.KeyboardKeys.Percent.toString();
        if (character.equals("^")) return PacketEncoding.KeyboardKeys.UpArrow.toString();
        if (character.equals("&")) return PacketEncoding.KeyboardKeys.Ampersand.toString();
        if (character.equals("*")) return PacketEncoding.KeyboardKeys.Star.toString();
        if (character.equals("(")) return PacketEncoding.KeyboardKeys.OpenParenthesis.toString();
        if (character.equals(")")) return PacketEncoding.KeyboardKeys.CloseParenthesis.toString();
        if (character.equals("?")) return PacketEncoding.KeyboardKeys.QuestionMark.toString();
        if (character.equals("<")) return PacketEncoding.KeyboardKeys.LessThan.toString();
        if (character.equals(">")) return PacketEncoding.KeyboardKeys.GreaterThan.toString();
        if (character.equals(":")) return PacketEncoding.KeyboardKeys.Colon.toString();
        if (character.equals("-")) return PacketEncoding.KeyboardKeys.Minus.toString();
        if (character.equals(";")) return PacketEncoding.KeyboardKeys.SemiColon.toString();
        if (character.equals("\"")) return PacketEncoding.KeyboardKeys.Quote.toString();
        if (character.equals("'")) return PacketEncoding.KeyboardKeys.Quote.toString();
        if (character.equals(".")) return PacketEncoding.KeyboardKeys.Period.toString();
        if (character.equals(",")) return PacketEncoding.KeyboardKeys.Comma.toString();
        if (character.equals("+")) return PacketEncoding.KeyboardKeys.Plus.toString();
        if (character.equals("=")) return PacketEncoding.KeyboardKeys.Equals.toString();
        if (character.equals("_")) return PacketEncoding.KeyboardKeys.Underscore.toString();
        if (character.equals("/")) return PacketEncoding.KeyboardKeys.ForwardSlash.toString();
        if (character.equals("]")) return PacketEncoding.KeyboardKeys.CloseSquareBracket.toString();
        if (character.equals("[")) return PacketEncoding.KeyboardKeys.OpenSquareBracket.toString();

        return null;
    }

    private Runnable clearKeyboardFeedbackRunnable = new Runnable() {
        @Override
        public void run() {
            tv_feedback.setText(null);
        }
    };
}
