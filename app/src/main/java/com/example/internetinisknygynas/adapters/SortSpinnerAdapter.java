package com.example.internetinisknygynas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.internetinisknygynas.R;
import com.example.internetinisknygynas.activities.BookListActivity;
import com.example.internetinisknygynas.adapters.BookListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortSpinnerAdapter extends ArrayAdapter<String> {
    private final List<Boolean> sortedItems;
    private SortSpinnerTextChangeListener listener;

    public SortSpinnerAdapter(BookListActivity context, List<String> items) {
        super(context, R.layout.spinner_item_checkbox, R.id.text_item, items);
        sortedItems = new ArrayList<>(Collections.nCopies(items.size(), false));
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sort_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text_item);

        String item = getItem(position);

        textView.setText(item);

        textView.setOnClickListener(view -> {
            listener.onTextChange(position, item);
        });

        return convertView;
    }

    public List<Boolean> getSortedItems() {
        return sortedItems;
    }

    public void setSortSpinnerTextChangeListener(SortSpinnerTextChangeListener listener) {
        this.listener = listener;
    }

    public interface SortSpinnerTextChangeListener {
        void onTextChange(int position, String item);
    }
}
