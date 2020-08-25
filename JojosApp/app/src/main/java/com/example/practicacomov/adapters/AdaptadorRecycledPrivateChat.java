package com.example.practicacomov.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemPrivateChat;

import java.util.ArrayList;

public class AdaptadorRecycledPrivateChat extends RecyclerView.Adapter<AdaptadorRecycledPrivateChat.ViewDatos> implements View.OnClickListener {
    ArrayList<ItemPrivateChat> listDatos;
    private View.OnClickListener listener;

    public AdaptadorRecycledPrivateChat(ArrayList<ItemPrivateChat> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_privatechat, null, false);
        view.setOnClickListener(this);
        return new ViewDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDatos holder, int position) {
        if (listDatos.get(position).getMensaje() == null) {
            holder.msg.setHeight(0);
            holder.date.setHeight(0);
            holder.msgP.setText(listDatos.get(position).getMensajepropio());
            holder.dateP.setText(listDatos.get(position).getDateP());
        } else {
            holder.msg.setText(listDatos.get(position).getMensaje());
            holder.date.setText(listDatos.get(position).getDate());
            holder.msgP.setHeight(0);
            holder.dateP.setHeight(0);
        }
        /*
        holder.msg.setText(listDatos.get(position).getMensaje());
        holder.date.setText(listDatos.get(position).getDate());
        holder.msgP.setText(listDatos.get(position).getMensajepropio());
        holder.dateP.setText(listDatos.get(position).getDateP());
        */
    }


    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) { this.listener = listener; }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewDatos extends RecyclerView.ViewHolder {
        TextView msg, msgP, date, dateP;

        public ViewDatos(@NonNull View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.messagePriv);
            msgP = (TextView) itemView.findViewById(R.id.messagePrivPropio);
            date = (TextView) itemView.findViewById(R.id.dateP);
            dateP = (TextView) itemView.findViewById(R.id.datePP);
        }
    }
}