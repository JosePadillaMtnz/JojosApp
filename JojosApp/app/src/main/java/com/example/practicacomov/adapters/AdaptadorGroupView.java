package com.example.practicacomov.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemGroupChat;

import java.util.ArrayList;

public class AdaptadorGroupView extends BaseAdapter {
    private Context context;
    private ArrayList<ItemGroupChat> listItems;

    public AdaptadorGroupView(Context context, ArrayList<ItemGroupChat> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {return listItems.size();}

    @Override
    public Object getItem(int position) {return listItems.get(position);}

    @Override
    public long getItemId(int position) {return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemGroupChat item = (ItemGroupChat) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_groupchat, null);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.messageChat);
        TextView tvUser = (TextView) convertView.findViewById(R.id.userChat);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateChat);
        TextView tvMessagePropio = (TextView) convertView.findViewById(R.id.messagePropio);
        TextView tvDateP = (TextView) convertView.findViewById(R.id.dateGPropio);

        if (item.getMensaje() == null) {
            tvMessage.setHeight(0);
            tvUser.setHeight(0);
            tvDate.setHeight(0);
            tvMessagePropio.setText(item.getMensajepropio());
            tvDateP.setText(item.getDateP());
        } else {
            tvMessage.setText(item.getMensaje());
            tvUser.setText(item.getUser());
            tvDate.setText(item.getDate());
            tvMessagePropio.setHeight(0);
            tvDateP.setHeight(0);
        }

        return convertView;
    }
}
