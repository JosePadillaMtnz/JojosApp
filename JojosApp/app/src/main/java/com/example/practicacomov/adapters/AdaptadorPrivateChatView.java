package com.example.practicacomov.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemPrivateChat;

import java.util.ArrayList;

public class AdaptadorPrivateChatView extends BaseAdapter {
    private Context context;
    private ArrayList<ItemPrivateChat> listItems;

    public AdaptadorPrivateChatView(Context context, ArrayList<ItemPrivateChat> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {return listItems.size();}

    @Override
    public Object getItem(int position) {return listItems.get(position);}

    @Override
    public long getItemId(int position) {return 0; }

    public void setListItems(ArrayList<ItemPrivateChat> lista) {
        this.listItems = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemPrivateChat item = (ItemPrivateChat) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_privatechat, null);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.messagePriv);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateP);
        TextView tvMessagePropio = (TextView) convertView.findViewById(R.id.messagePrivPropio);
        TextView tvDateP = (TextView) convertView.findViewById(R.id.datePP);

        if (item.getMensaje() == null) {
            tvMessage.setHeight(0);
            tvDate.setHeight(0);
            tvMessagePropio.setText(item.getMensajepropio());
            tvDateP.setText(item.getDateP());
        } else {
            tvMessage.setText(item.getMensaje());
            tvDate.setText(item.getDate());
            tvMessagePropio.setHeight(0);
            tvDateP.setHeight(0);
        }

        return convertView;
    }
}
