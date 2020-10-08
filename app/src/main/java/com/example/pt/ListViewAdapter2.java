package com.example.pt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter2 extends ArrayAdapter<com.example.pt.ListData> {
    private LayoutInflater layoutInflater;

    public ListViewAdapter2(Context context, int resource, List<com.example.pt.ListData> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.example.pt.ListData data = getItem(position);
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView dateText;
        TextView nameText;
        TextView starttimeText;
        TextView closingtimeText;
        TextView getpointText;

        dateText = convertView.findViewById(R.id.date);
        nameText = convertView.findViewById(R.id.name);
        starttimeText = convertView.findViewById(R.id.starttime);
        closingtimeText = convertView.findViewById(R.id.closingtime);
        getpointText = convertView.findViewById(R.id.getpoint);

        dateText.setText(data.getDate());
        nameText.setText(data.getName());
        starttimeText.setText(data.getStarttime());
        closingtimeText.setText(data.getClosingtime());
        getpointText.setText(data.getGetpoint());

        return convertView;
    }
}