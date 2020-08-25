package com.example.practicacomov.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicacomov.R;
import com.example.practicacomov.models.ItemContactsView;

import java.util.ArrayList;

public class AdaptadorRecycledContacts extends RecyclerView.Adapter<AdaptadorRecycledContacts.ViewDatos> implements View.OnClickListener {
    ArrayList<ItemContactsView> listDatos;
    private View.OnClickListener listener;

    public AdaptadorRecycledContacts(ArrayList<ItemContactsView> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contactsview
                , null, false);
        view.setOnClickListener(this);
        return new ViewDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDatos holder, int position) {
        holder.image.setImageResource(listDatos.get(position).getImage());
        holder.userName.setText(listDatos.get(position).getUser());
        holder.realName.setText(listDatos.get(position).getRealUser());
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
        TextView userName, realName;

        public ViewDatos(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imgFoto);
            userName = (TextView) itemView.findViewById(R.id.nameUser);
            realName = (TextView) itemView.findViewById(R.id.realNameUser);
        }
    }
}
