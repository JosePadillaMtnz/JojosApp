package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.practicacomov.adapters.AdaptadorRecycledListChat;
import com.example.practicacomov.models.ItemListView;

import java.util.ArrayList;

public class PruebaRecycledVIew extends AppCompatActivity {
    ArrayList<ItemListView> listDatos;
    RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_recycled_v_iew);

        recycler = (RecyclerView) findViewById(R.id.recyclervista);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        listDatos = new ArrayList<ItemListView>();

        llenarItems();

        AdaptadorRecycledListChat adapter = new AdaptadorRecycledListChat(listDatos);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Selección: "+listDatos.get(recycler.getChildAdapterPosition(view))
                                .getUser(),Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);

    }

    private void llenarItems() {
        listDatos.add(new ItemListView(R.drawable.jojo_email, "MakinaShout", "Bueno precioso como vas?", "23:30"));
        listDatos.add(new ItemListView(R.drawable.ic_camera_icon, "TuCamaraFav", "Sonrieeee", "11:56"));
        listDatos.add(new ItemListView(R.drawable.jojo_register, "Tu Jojo de confianza", "Bueno precioso como vas?", "21/08/56 15:56"));
    }
}
