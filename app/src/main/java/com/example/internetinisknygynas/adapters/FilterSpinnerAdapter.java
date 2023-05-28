package com.example.internetinisknygynas.adapters;

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

public class FilterSpinnerAdapter extends ArrayAdapter<String> {
    private final List<Boolean> checkedItems;
    private CheckboxChangeListener listener;
    private String string = "";

    public FilterSpinnerAdapter(BookListActivity context, List<String> items, BookListAdapter bookListAdapter) {
        super(context, R.layout.spinner_item_checkbox, R.id.text_item, items);
        checkedItems = new ArrayList<>(Collections.nCopies(items.size(), false));
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.spinner_item_checkbox, parent, false);
        }

        CheckBox checkbox = convertView.findViewById(R.id.checkbox_item);
        TextView textView = convertView.findViewById(R.id.text_item);

        String item = getItem(position);
        boolean isChecked = checkedItems.get(position);

        checkbox.setChecked(isChecked);
        textView.setText(item);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!item.isEmpty()){
                    listener.onCheckboxChanged(position, item, b, checkedItems);
                }
            }
        });

        return convertView;
    }

    public List<Boolean> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckboxChangeListener(CheckboxChangeListener listener) {
        this.listener = listener;
    }

    public interface CheckboxChangeListener {
        void onCheckboxChanged(int position, String item, boolean b, List<Boolean> checkedItems);
    }
}
