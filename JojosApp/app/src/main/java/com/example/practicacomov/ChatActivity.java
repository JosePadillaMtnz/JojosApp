package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.adapters.AdaptadorRecycledListChat;
import com.example.practicacomov.models.ItemListView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {
    ArrayList<ItemListView> listDatos;
    RecyclerView recycler;
    private static String authToken;
    private static APIService apiService;
    private static String username;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        apiService = RetrofitClient.getClient(getString(R.string.baseUrl), this).create(APIService.class);

        try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            sharedPreferences = EncryptedSharedPreferences.create(getString(R.string.encrypted_shared_preferences_name),
                                                                  masterKeyAlias,
                                                                  getApplicationContext(),
                                                                  EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                                  EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("saveUser", false))
            username = sharedPreferences.getString("user", null);
        else
            username = getIntent().getExtras().get("username").toString();

        authToken = sharedPreferences.getString("authToken", null);
        Log.i("Chat Activity authToken", authToken);
        listDatos = new ArrayList<ItemListView>();
        getItems();

        recycler = (RecyclerView) findViewById(R.id.recycledChat);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorRecycledListChat adapter = new AdaptadorRecycledListChat(listDatos);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Selected: "+listDatos.get(recycler.getChildAdapterPosition(view))
                                .getUser(),Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);
    }

    private void getItems(){
        listDatos.add(new ItemListView(R.drawable.jojo_email, "MakinaShout", "Helloo", "23:30"));
        listDatos.add(new ItemListView(R.drawable.ic_camera_icon, "TuCamFav", "Enjoyy", "11:56"));
        listDatos.add(new ItemListView(R.drawable.jojo_register, "JojoUUUser", "Eyyy!", "21/08/56 15:56"));
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("authToken", authToken);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void recoverMessages(View view){
        Intent intent = new Intent(this, RecoverMessagesActivity.class);
        intent.putExtra("authToken", authToken);
        intent.putExtra("username", username);
        intent.putExtra("userchat", "makinashout");
        startActivity(intent);
    }

    public void openContacts(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("authToken", authToken);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openPrivateChat(View view) {
        Intent intent = new Intent(this, PrivateChatActivity.class);
        intent.putExtra("authToken", authToken);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openGroupChat(View view) {
        Intent intent = new Intent(this, GroupChatActivity.class);
        intent.putExtra("authToken", authToken);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void cancel(View view) {
        super.finish();
    }

    private class MessagesThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
