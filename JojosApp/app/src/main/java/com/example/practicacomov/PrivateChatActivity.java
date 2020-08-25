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
import com.example.practicacomov.adapters.AdaptadorRecycledPrivateChat;
import com.example.practicacomov.models.ItemPrivateChat;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class PrivateChatActivity extends AppCompatActivity {
    ArrayList<ItemPrivateChat> listDatos;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        listDatos = new ArrayList<ItemPrivateChat>();
        getItems();

        recycler = findViewById(R.id.recyclerPrivateChat);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorRecycledPrivateChat adapter = new AdaptadorRecycledPrivateChat(listDatos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Selección: ",Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);
    }

    private void getItems() {
        listDatos.add(new ItemPrivateChat("Holaa!!", "[12/04/2020] 23:34", "patata", null));
        listDatos.add(new ItemPrivateChat(null,null,"Como te va todo?", "[12/04/2020] 23:34"));
        listDatos.add(new ItemPrivateChat("Pues la verdad que bien tio, el otro día fui a hacer la compra y me encontré a Ralph.", "[12/04/2020] 23:35", null, null));
        listDatos.add(new ItemPrivateChat(null, null,"Y que te dijo?", "[12/04/2020] 23:36"));
        listDatos.add(new ItemPrivateChat("Corre plátano!", "[12/04/2020] 23:36", null,null));
    }

    public void sendMessage(View view) {
        //todo
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}