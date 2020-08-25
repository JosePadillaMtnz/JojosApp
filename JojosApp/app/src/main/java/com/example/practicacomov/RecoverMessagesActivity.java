package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.adapters.AdaptadorRecycledListChat;
import com.example.practicacomov.models.ItemListView;
import com.example.practicacomov.models.ItemPrivateChat;
import com.example.practicacomov.models.Message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Thread.sleep;

public class RecoverMessagesActivity extends AppCompatActivity {

    ArrayList<ItemListView> listDatos;
    RecyclerView recycler;
    private Retrofit retrofit;
    private String authToken;
    private String username;
    private String userchat;
    private String filename;
    private String lastMessageReceived;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shareEditor;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_messages);
        apiService = RetrofitClient.getClient(getString(R.string.baseUrl), this).create(APIService.class);
        authToken = getIntent().getExtras().get("authToken").toString();
        //userchat = getIntent().getExtras().get("userchat").toString();
        //filename = userchat + ".txt";
        username = getIntent().getExtras().get("username").toString();
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
        lastMessageReceived = sharedPreferences.getString("lastMessageReceived", null);
        createNotificationChannel();
        new MessagesThread().start();
        getMessages();
        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        listDatos = new ArrayList<ItemListView>();
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

    public void patata(View view) {
        ArrayList<ItemPrivateChat> pepe = new ArrayList<ItemPrivateChat>();
        listDatos.add(new ItemListView(R.drawable.jojo_email, "hola", "adios", "prueba"));
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

    public void prueba(View view) {
        Message manolo = new Message("Me guardo?", "Makina", "Me", Message.TYPE_PLAINTEXT);
        manolo.setDate(new Date(0,0,0,0,0,0));
        addMessageFile(manolo);
        Message maonlin = new Message("Me guardo algo?", "Makina", "Me", Message.TYPE_PLAINTEXT);
        maonlin.setDate(new Date(120,3,14,19,56,1));
        addMessageFile(maonlin);
        getMessageFile();
    }

    public void refreshView() {
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

    public void addMessageFile(Message message) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(filename, Activity.MODE_APPEND));
            archivo.write(message.getBody() + "-----" + "[date]" + "\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {

        }
    }

    public void getMessageFile() {
        String archivos[] = fileList();

        if (existeArchivo(archivos, filename)) {
            //LinkedList<ItemPrivateChat> list = new LinkedList<ItemPrivateChat>();
            try{
                InputStreamReader archivo = new InputStreamReader(openFileInput(filename));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String contenido = "";

                while (linea != null) {
                    /*
                    String line = linea;
                    String[] lin = line.split("-----");
                    ItemPrivateChat item = new ItemPrivateChat(lin[0], lin[1], null, null);
                    list.add(item);
                     */
                    contenido = contenido + linea + "\n";
                    linea = br.readLine();
                }

                Log.e("Contenido ->", contenido);
                br.close();
                archivo.close();
                //return list;
            } catch (IOException e) {

            }
        }
        //return null;
    }

    public boolean existeArchivo(String archivos[], String name) {
        for (int i = 0; i < archivos.length; i++) {
            if (name.equals(archivos[i])) return true;
        }
        return false;
    }

    public String formatDate(Date date) {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        String datefinal = "[" + date.getDate() + "/" + month + "/" + year + "] " + date.getHours() + ":" + date.getMinutes();
        return datefinal;
    }

    public void getMessages() {


        if (lastMessageReceived == null) {
            Date date = new Date(0,0,0,0,0,0);
            Message message = new Message(null, null, null, Message.TYPE_PLAINTEXT);
            message.setDate(date);
            lastMessageReceived = message.toString();
            //lastMessageReceived = message.getDate();
        }

        //Call<JsonObject> call = apiService.getMessagesFromTo(username, "Makinashout", authToken);
        Call<JsonObject> call = apiService.getMessages(username, lastMessageReceived, authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonArray messages = response.body().get("MessagesObj").getAsJsonArray();
                    Log.e("Array messages ->",messages.toString());
                    Log.e("Size messages ->", String.valueOf(messages.size()));
                    for(int i=0; i<messages.size(); i++) {
                        Message message = new Gson().fromJson(messages.get(i), Message.class);
                        /*
                        boolean pepe = true;
                        if (!message.getFrom().equals("Joselito")) {
                            item = new ItemPrivateChat(message.getBody(), message.getDate().toString(), null, null);
                            for (ItemPrivateChat itemm : listDatos) {
                                if (itemm.getMensaje() != null) {
                                    if (itemm.getMensaje().equals(item.getMensaje())) pepe = false;
                                }
                            }
                        } else {
                            item = new ItemPrivateChat(null, null, message.getBody(), message.getDate().toString());
                            for (ItemPrivateChat itemm : listDatos) {
                                if (itemm.getMensajepropio() != null) {
                                    if (itemm.getMensajepropio().equals(item.getMensajepropio())) pepe = false;
                                }
                            }
                        }
                           */

                        if (message.getType() == Message.TYPE_LOCATION) {
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            intent.putExtra("username", message.getFrom());
                            intent.putExtra("isFromMessage", true);
                            intent.putExtra("location", message.getBody());
                            startActivity(intent);
                        }

                        //listDatos.add(new ItemListView(R.drawable.jojo_pass, message.getFrom(), message.getBody(), "message.getDate()"));
                        //addMessageFile(message);
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(0, createBasicNotification(message.getFrom(), message.getBody(), NotificationCompat.PRIORITY_HIGH));


                        /*
                        if (message.getFrom().equals("Joselito")) {
                            list.add(new ItemPrivateChat(null, null, message.getBody(), "date"));+
                        } else {
                            list.add(new ItemPrivateChat(message.getBody(), "date", null,null));
                        }
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(0, createBasicNotification(message.getFrom(), message.getBody(), NotificationCompat.PRIORITY_HIGH));

                        */

                        if (i==0) {
                            lastMessageReceived = message.toString();
                            shareEditor = sharedPreferences.edit();
                            shareEditor.putString("lastMessageReceived", lastMessageReceived);
                            shareEditor.apply();
                        }

                    }
                    refreshView();
                }
                else Log.e("Failed to recover messages", response.message());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Recover messages failure", t.getMessage());
            }
        });
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.channel_id),
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.channel_description));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private Notification createBasicNotification(String title, String content, int priority) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(priority)
                .setAutoCancel(true);
        return builder.build();
    }

    private class MessagesThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getMessages();
            }
        }
    }
}
