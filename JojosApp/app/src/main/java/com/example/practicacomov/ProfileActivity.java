package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.models.ModifyPetition;
import com.example.practicacomov.models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String username;
    private APIService apiService;
    private String authToken;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        else username = getIntent().getExtras().getString("username");

        authToken = sharedPreferences.getString("authToken", null);
        apiService = RetrofitClient.getClient(getString(R.string.baseUrl), this).create(APIService.class);
    }

    public void cancel(View view) {
        super.finish();
    }

    public void modifyPassword(View view) {
        TextView currentPasswordText = findViewById(R.id.currentPasswordText);
        TextView newPasswordText = findViewById(R.id.newPasswordText);

        String salt = null;
        for (String salts : sharedPreferences.getStringSet("salts", new ArraySet<String>())) {
            if (salts.split(":")[0].equals(username)) {
                salt = salts.split(":")[1];
                break;
            }
        }
        String hashCurrentPassword = Security.hashPassword(currentPasswordText.getText().toString(), Security.fromHex(salt));
        String currentPassword = hashCurrentPassword.split(":")[1];
        final String newPassword = newPasswordText.getText().toString();
        final String saltCopy = salt;
        Call<JsonPrimitive> call = apiService.login(new User(username, null, currentPassword));
        call.enqueue(new Callback<JsonPrimitive>() {
            @Override
            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                if (response.isSuccessful()) {
                    authToken = response.body().toString();
                    editor.putString("authToken", authToken);
                    editor.apply();
                    final String hashNewPassword = Security.hashPassword(newPassword, null);
                    Call<JsonObject> modifyCall = apiService.modifyUser(new ModifyPetition(username, "pHash", hashNewPassword.split(":")[1]), authToken);
                    modifyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Set<String> salts = sharedPreferences.getStringSet("salts", new ArraySet<String>());
                                salts.remove(username + ":" + saltCopy);
                                salts.add(username + ":" + hashNewPassword.split(":")[0]);
                                editor.putStringSet("salts", salts);
                                editor.apply();
                                Toast.makeText(ProfileActivity.this, getString(R.string.passwordChanged), Toast.LENGTH_LONG).show();
                            }

                            else {
                                Toast.makeText(ProfileActivity.this, getString(R.string.changePasswordError), Toast.LENGTH_LONG).show();
                                Log.e("modifyPassword error", response.message());
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this, getString(R.string.communicationError), Toast.LENGTH_LONG).show();
                            Log.e("modifyPassword failure", t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.wrongPassword), Toast.LENGTH_LONG).show();
                    Log.e("modifyPassword error", response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, getString(R.string.communicationError), Toast.LENGTH_LONG).show();
                Log.e("modifyPassword login failure", t.getMessage());
            }
        });
    }

    public void modifyEmail(View view) {
        TextView newEmailText = findViewById(R.id.newEmailText);

        Call<JsonObject> call = apiService.modifyUser(new ModifyPetition(username, "email", newEmailText.getText().toString()), authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful())
                    Toast.makeText(ProfileActivity.this, getString(R.string.emailChangedSuccessfully), Toast.LENGTH_LONG).show();
                else if (response.code() == 409)
                    Toast.makeText(ProfileActivity.this, getString(R.string.emailRegisteredError), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ProfileActivity.this, getString(R.string.unexpectedError), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, getString(R.string.communicationError), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(View view) {
        editor.remove("username");
        editor.remove("pHash");
        editor.remove("authToken");
        editor.putBoolean("saveUser", false);
        editor.apply();
        Toast.makeText(this, getString(R.string.logoutMessage), Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void deleteUser(View view) {
        Call<JsonObject> call = apiService.deleteUser(username, authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    editor.remove("username");
                    editor.remove("pHash");
                    editor.remove("authToken");
                    editor.putBoolean("saveUser", false);
                    editor.apply();
                    Toast.makeText(ProfileActivity.this, getString(R.string.userDeleted), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else Toast.makeText(ProfileActivity.this, getString(R.string.wrongPassword), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, getString(R.string.communicationError), Toast.LENGTH_LONG).show();
            }
        });
    }
}
