/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Dialogs.Help
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/31/17 8:52 PM
 */
package com.linoagli.android.xadditus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.linoagli.android.xadditus.R;

public class HelpPageFragment extends Fragment {
    public static final String TAG = HelpPageFragment.class.getName();

    private String content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_page, container, false);

        TextView tv_content = (TextView) view.findViewById(R.id.tv_helpPage_content);
        tv_content.setText(content);

        return view;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
