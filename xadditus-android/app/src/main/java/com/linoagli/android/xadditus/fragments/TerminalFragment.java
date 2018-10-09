/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 1/24/16 1:03 AM
 */
package com.linoagli.android.xadditus.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.linoagli.android.xadditus.DataReceivedEvent;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.SendRequestEvent;
import com.linoagli.android.xadditus.utils.TerminalEntry;
import com.linoagli.android.xadditus.adapters.TerminalEntriesListAdapter;
import com.linoagli.java.XadditusCore.Constants;
import com.linoagli.java.XadditusCore.PacketEncoding;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.StringTokenizer;

public class TerminalFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rv_entries;
    private EditText et_command;
    private Button bt_run;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);

        rv_entries = view.findViewById(R.id.rv_terminal_entries);
        et_command = view.findViewById(R.id.et_terminal_command);
        bt_run = view.findViewById(R.id.bt_terminal_run);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);

        rv_entries.setLayoutManager(linearLayoutManager);
        rv_entries.setAdapter(new TerminalEntriesListAdapter());

        // Setting up listeners
        bt_run.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        runCommand();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataReceivedEvent event) {
        try {
            StringTokenizer st = new StringTokenizer(event.getData(), Constants.STRING_TOKENIZER_DELIMITOR);
            String packetTypeString = st.nextToken();
            PacketEncoding.PacketType packetType = PacketEncoding.PacketType.valueOf(packetTypeString);

            if (packetType == PacketEncoding.PacketType.TerminalVerboseOutput) {
                addTerminalEntry(new TerminalEntry(TerminalEntry.Type.VerboseOutput, st.nextToken()));
            }
            if (packetType == PacketEncoding.PacketType.TerminalErrorOutput) {
                addTerminalEntry(new TerminalEntry(TerminalEntry.Type.ErrorOutput, st.nextToken()));
            }
        } catch (Exception e) {
            //
        }
    }

    private void runCommand() {
        String command = et_command.getText().toString();

        StringBuilder sb = new StringBuilder();
        sb.append(PacketEncoding.PacketType.TerminalCommand.toString()).append(Constants.STRING_TOKENIZER_DELIMITOR);
        sb.append(command).append(Constants.STRING_TOKENIZER_DELIMITOR);

        EventBus.getDefault().post(new SendRequestEvent(sb.toString(), false));

        addTerminalEntry(new TerminalEntry(TerminalEntry.Type.Command, command));
        et_command.setText(null);
    }

    private void addTerminalEntry(final TerminalEntry entry) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TerminalEntriesListAdapter adapter = (TerminalEntriesListAdapter) rv_entries.getAdapter();
                adapter.addEntry(entry);
                rv_entries.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }
}
