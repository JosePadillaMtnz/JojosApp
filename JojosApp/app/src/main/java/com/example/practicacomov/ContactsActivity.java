package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.practicacomov.adapters.AdaptadorRecycledContacts;
import com.example.practicacomov.models.ItemContactsView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    ArrayList<ItemContactsView> listDatos;
    RecyclerView recycler;

    String delim = "--__--";
    private String fileContacts = "ContactsList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listDatos = new ArrayList<ItemContactsView>();
        getContactsFile();
        getItems();
        recycler = (RecyclerView) findViewById(R.id.recycledContacts);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorRecycledContacts adapter = new AdaptadorRecycledContacts(listDatos);
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

    private void getItems(){
        listDatos.add(new ItemContactsView(R.drawable.jojo_email, "Aqui el nombre que le des", "Aqui su id real"));
        listDatos.add(new ItemContactsView(R.drawable.jojo_pass, "Username bro", "Realname"));
        listDatos.add(new ItemContactsView(R.drawable.ic_delete_icon, "Mi nombre", "El real?"));
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void addContact(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    public void getContactsFile() {
        Log.e("Lo intento ->", listDatos.toString());
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput(fileContacts));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            while (linea != null) {
                String[] lin = linea.split(delim);
                int image;
                if (lin[0].equals("default")) image = R.drawable.jojo_user;
                else image = R.drawable.jojo_email;
                ItemContactsView item = new ItemContactsView(image, lin[1], lin[2]);
                listDatos.add(item);
                linea = br.readLine();
            }
            Log.e("Contenido ->", listDatos.toString());
            br.close();
            archivo.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception ->", listDatos.toString());
        }
    }
}