package com.example.practicacomov.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemGroupChat;

import java.util.ArrayList;

public class AdaptadorRecycledGroupChat extends RecyclerView.Adapter<AdaptadorRecycledGroupChat.ViewDatos> implements View.OnClickListener {
    ArrayList<ItemGroupChat> listDatos;
    private View.OnClickListener listener;

    public AdaptadorRecycledGroupChat(ArrayList<ItemGroupChat> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupchat, null, false);
        view.setOnClickListener(this);
        return new ViewDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDatos holder, int position) {
        if (listDatos.get(position).getUser() == null) {
            holder.msgP.setText(listDatos.get(position).getMensajepropio());
            holder.dateP.setText(listDatos.get(position).getDateP());
            holder.msg.setHeight(0);
            holder.date.setHeight(0);
            holder.user.setHeight(0);
        } else {
            holder.user.setText(listDatos.get(position).getUser());
            holder.msg.setText(listDatos.get(position).getMensaje());
            holder.date.setText(listDatos.get(position).getDate());
            holder.msgP.setHeight(0);
            holder.dateP.setHeight(0);
        }
        /*
        holder.user.setText(listDatos.get(position).getUser());
        holder.msg.setText(listDatos.get(position).getMensaje());
        holder.msgP.setText(listDatos.get(position).getMensajepropio());
        holder.date.setText(listDatos.get(position).getDate());
        holder.dateP.setText(listDatos.get(position).getDateP());
        */
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewDatos extends RecyclerView.ViewHolder {
        TextView user, msg, msgP, date, dateP;

        public ViewDatos(@NonNull View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.userChat);
            msg = (TextView) itemView.findViewById(R.id.messageChat);
            msgP = (TextView) itemView.findViewById(R.id.messagePropio);
            date = (TextView) itemView.findViewById(R.id.dateChat);
            dateP = (TextView) itemView.findViewById(R.id.dateGPropio);
        }
    }
}
