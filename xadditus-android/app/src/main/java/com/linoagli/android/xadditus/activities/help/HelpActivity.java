/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Dialogs
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/31/17 8:38 PM
 */
package com.linoagli.android.xadditus.activities.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.base.BaseActivity;
import com.linoagli.compoundviews.ViewPagerExtended;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private ViewPagerExtended viewPager;
    private Button bt_prev;
    private Button bt_next;

    private HelpPagesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        viewPager = (ViewPagerExtended) findViewById(R.id.fw_help_pages);
        bt_prev = (Button) findViewById(R.id.bt_help_prev);
        bt_next = (Button) findViewById(R.id.bt_help_next);

        // Setting up the view pager
        adapter = new HelpPagesAdapter(getSupportFragmentManager());
        viewPager.setSwipeGestureEnabled(false);
        viewPager.setAdapter(adapter);

        // Setting up the listeners
        bt_prev.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_prev) {
            int index = viewPager.getCurrentItem();
            if (index > 0) viewPager.setCurrentItem(index - 1);
        }

        if (v == bt_next) {
            int index = viewPager.getCurrentItem();
            if (index < adapter.getCount() - 1) viewPager.setCurrentItem(index + 1);
        }
    }
}
