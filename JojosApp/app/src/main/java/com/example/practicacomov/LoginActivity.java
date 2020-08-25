package com.example.practicacomov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.models.Message;
import com.example.practicacomov.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private String authToken;
    private SharedPreferences sharedPreferences;
    private String user;
    private String pHash;
    private APIService apiService;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        authToken = sharedPreferences.getString("authToken", null);

        if (sharedPreferences.getBoolean("saveUser", false)) {
            user = sharedPreferences.getString("user", null);
            pHash = sharedPreferences.getString("pHash", null);
        }
        else {
            user = getIntent().getExtras().get("username").toString();
            pHash = getIntent().getExtras().get("pHash").toString();
        }

        editor = sharedPreferences.edit();

         /*
        APIService apiService = retrofit.create(APIService.class);
        Call<JsonObject> call = apiService.getUser(authtoken, user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.i("MESSAGE GETUSER OK",response.body().toString());
                    Gson gson = new Gson();
                    all_user = gson.fromJson(response.body(), User.class);
                    Log.i("ALL USER", all_user.getEmail());
                    if (all_user.getImage().length() > 1) {
                        byte [] encodeByte = Base64.decode(all_user.getImage(),Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        ImageView imvw = findViewById(R.id.imageuser);
                        imvw.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
                    }
                }
                else {
                    Log.e("MESSAGE GETUSER FAILURE", response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FAILURE:", t.getMessage());
            }
        });
         */
    }

    public void sendMessage(View view) {
        TextView bodyMessageView = findViewById(R.id.bodyTextMessage);
        TextView toMessageView = findViewById(R.id.toTextMessage);

        Message message = new Message(bodyMessageView.getText().toString(),
                                      user,
                                      toMessageView.getText().toString(),
                                      Message.TYPE_PLAINTEXT);
        //message.setDate(new Date(120,3,14,19,56,1));

        Call<JsonObject> call = apiService.sendMessage(message, authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.i("MESSAGE SEND OK",response.body().toString());
                } else {
                    if (response.code() == 401) {
                        Log.i("Non authorized", authToken);
                        login(user, pHash);
                        authToken = sharedPreferences.getString("authToken", null);
                        sendMessage(findViewById(R.id.layoutLogin));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FAILURE:", t.getMessage());
            }
        });
    }

    public void sendMapMessage(View view) {
        final TextView toMessageView = findViewById(R.id.toTextMessage);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        Message message = new Message(location.getLatitude() + "\n" + location.getLongitude(),
                                                      user,
                                                      toMessageView.getText().toString(),
                                                      Message.TYPE_LOCATION);
                        Call<JsonObject> call = apiService.sendMessage(message, authToken);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    Log.i("MESSAGE MAP SEND OK",response.body().toString());
                                } else {
                                    if (response.code() == 401) {
                                        Call<JsonPrimitive> callLogin = apiService.login(new User(user, null, pHash));
                                        callLogin.enqueue(new Callback<JsonPrimitive>() {
                                            @Override
                                            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                                                if (response.isSuccessful()) {
                                                    authToken = response.body().toString();
                                                    sendMessage(findViewById(R.id.layoutLogin));
                                                }
                                                else authToken = null;
                                                editor.putString("authToken", authToken);
                                                editor.apply();
                                            }

                                            @Override
                                            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                                                Log.e("LoginActivity login error", t.getMessage());
                                            }
                                        });

                                    }
                                }
                             }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.i("MESSAGE MAP SEND FAILURE", t.getMessage());
                            }
                        });
                    }
                }
            });
        }
    }

    private void login(String username, String pHash) {

    }

}
