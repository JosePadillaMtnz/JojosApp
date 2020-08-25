package com.example.practicacomov.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemListView;

import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {
    private Context context;
    private ArrayList<ItemListView> listItems;

    public AdaptadorListView(Context context, ArrayList<ItemListView> listItems){
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemListView item = (ItemListView) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.imgFotoC);
        TextView tvUser = (TextView) convertView.findViewById(R.id.nameUserC);
        TextView tvLastMessage = (TextView) convertView.findViewById(R.id.lastMessageC);
        TextView tvDate = (TextView) convertView.findViewById(R.id.date);

        image.setImageResource(item.getImage());
        tvUser.setText(item.getUser());
        tvLastMessage.setText(item.getLastMessage());
        tvDate.setText(item.getDate());

        return convertView;
    }
}
