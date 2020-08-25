package com.example.practicacomov.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemContactsView;

import java.util.ArrayList;

public class AdaptadorContactsView extends BaseAdapter {
    private Context context;
    private ArrayList<ItemContactsView> listItems;

    public AdaptadorContactsView(Context context, ArrayList<ItemContactsView> listItems){
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
        ItemContactsView item = (ItemContactsView) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_contactsview, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.imgFoto);
        TextView tvUser = (TextView) convertView.findViewById(R.id.nameUser);
        TextView tvLastMessage = (TextView) convertView.findViewById(R.id.realNameUser);

        image.setImageResource(item.getImage());
        tvUser.setText(item.getUser());
        tvLastMessage.setText(item.getRealUser());

        return convertView;
    }
}
