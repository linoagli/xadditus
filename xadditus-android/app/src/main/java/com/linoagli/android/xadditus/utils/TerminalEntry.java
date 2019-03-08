/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments.Terminal
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/31/17 6:14 PM
 */
package com.linoagli.android.xadditus.utils;

import android.graphics.Typeface;
import com.linoagli.android.xadditus.R;

public class TerminalEntry {
    public Type type;
    public String value;

    public TerminalEntry(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        Command(Typeface.BOLD, R.color.textColor_grayish),
        VerboseOutput(Typeface.NORMAL, R.color.textColor_blackish),
        ErrorOutput(Typeface.NORMAL, R.color.textColor_redish);

        public final int style;
        public final int textColor;

        Type(int style, int textColor) {
            this.style = style;
            this.textColor = textColor;
        }
    }
}
