package com.example.practicacomov.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemListView;

import java.util.ArrayList;

public class AdaptadorRecycledListChat extends RecyclerView.Adapter<AdaptadorRecycledListChat.ViewDatos> implements View.OnClickListener {
    ArrayList<ItemListView> listDatos;
    private View.OnClickListener listener;

    public AdaptadorRecycledListChat(ArrayList<ItemListView> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, null, false);
        view.setOnClickListener(this);
        return new ViewDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDatos holder, int position) {
        holder.image.setImageResource(listDatos.get(position).getImage());
        holder.tvUser.setText(listDatos.get(position).getUser());
        holder.tvLastMessage.setText(listDatos.get(position).getLastMessage());
        holder.tvDate.setText(listDatos.get(position).getDate());
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
        ImageView image;
        TextView tvUser, tvLastMessage, tvDate;

        public ViewDatos(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgFotoC);
            tvUser = (TextView) itemView.findViewById(R.id.nameUserC);
            tvLastMessage = (TextView) itemView.findViewById(R.id.lastMessageC);
            tvDate = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
