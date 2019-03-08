/**
 * Xadditus Android Project.
 * com.linoagli.android.Xadditus.Activities.InputInterface.Fragments.Terminal
 *
 * @author Olubusayo K. Faye-Lino Agli, username: linoagli
 * @since 3/31/17 6:11 PM
 */
package com.linoagli.android.xadditus.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.linoagli.android.xadditus.R;
import com.linoagli.android.xadditus.utils.TerminalEntry;

import java.util.ArrayList;
import java.util.List;

public class TerminalEntriesListAdapter extends RecyclerView.Adapter<TerminalEntriesListAdapter.ViewHolder> {
    private final int MAX_ENTRIES = 200;

    private List<TerminalEntry> entries = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.partial_terminal_entry_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TerminalEntry entry = entries.get(position);

        int markerVisibility = (entry.type == TerminalEntry.Type.Command) ? View.VISIBLE : View.INVISIBLE;
        holder.tv_commandMarker.setVisibility(markerVisibility);

        holder.tv_value.setTypeface(Typeface.MONOSPACE, entry.type.style);
        holder.tv_value.setTextColor(holder.itemView.getContext().getColor(entry.type.textColor));
        holder.tv_value.setText(entry.value);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void addEntry(TerminalEntry entry) {
        entries.add(entry);
        if (entries.size() > MAX_ENTRIES) entries.remove(0);

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_commandMarker;
        TextView tv_value;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_commandMarker = (TextView) itemView.findViewById(R.id.tv_terminalEntryListItem_commandMarker);
            tv_value = (TextView) itemView.findViewById(R.id.tv_terminalEntryListItem_value);
        }
    }
}