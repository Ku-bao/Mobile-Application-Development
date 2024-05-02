package com.example.securenotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    private List<Note> backlist;
    private List<Note> noteList;

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        this.backlist = noteList;
    }
    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Note getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.note_layout, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvTime.setText(note.getTime());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Note> filteredResults;
                if (constraint == null || constraint.length() == 0) {
                    filteredResults = backlist;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                noteList = (List<Note>) results.values;
                notifyDataSetChanged();
            }

            private List<Note> getFilteredResults(String searchWord) {
                List<Note> resultList = new ArrayList<>();
                for (Note note : backlist) {
                    if (note.getTitle().toLowerCase().contains(searchWord)) {
                        resultList.add(note);
                    }
                }
                return resultList;
            }
        };
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
    }

}
