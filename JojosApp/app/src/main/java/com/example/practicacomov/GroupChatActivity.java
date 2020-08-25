package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.adapters.AdaptadorRecycledGroupChat;
import com.example.practicacomov.models.ItemGroupChat;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class GroupChatActivity extends AppCompatActivity {
    ArrayList<ItemGroupChat> listDatos;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        listDatos = new ArrayList<ItemGroupChat>();
        getItems();

        recycler = findViewById(R.id.recycledGroupChat);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorRecycledGroupChat adapter = new AdaptadorRecycledGroupChat(listDatos);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Selección: En group",Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);
    }

    private void getItems() {
        listDatos.add(new ItemGroupChat("Lucas", "Bienvenido", "[13/04/2020] 0:17", null, null));
        listDatos.add(new ItemGroupChat("Pepe", "Holaaa!", "[13/04/2020] 0:18", null, null));
        listDatos.add(new ItemGroupChat(null,null,null, "Como va todo?", "[13/04/2020] 0:19"));
        listDatos.add(new ItemGroupChat("Maria", "Arribaaaa!", "[13/04/2020] 0:19", null, null));
    }

    public void sendMessage(View view) {
        //todo
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
