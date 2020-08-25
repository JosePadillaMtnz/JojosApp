package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.security.keystore.KeyGenParameterSpec;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.models.User;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.MissingFormatArgumentException;
import java.util.Set;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    private APIService apiService;
    private byte[] imageuser = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apiService = RetrofitClient.getClient(getString(R.string.baseUrl), this).create(APIService.class);
        Button btn = findViewById(R.id.deletebtn);
        btn.setVisibility(View.INVISIBLE);
        try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            sharedPreferences = EncryptedSharedPreferences.create("com.example.jojosapp_encrypted_shared_preferences", masterKeyAlias, getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        TextView userText = findViewById(R.id.userText);
        TextView passwordText = findViewById(R.id.passwordText);
        TextView passwordText2 = findViewById(R.id.passwordConf);
        TextView emailText = findViewById(R.id.emailText);

        String username = userText.getText().toString();
        String password = passwordText.getText().toString();
        String password2 = passwordText2.getText().toString();
        String email = emailText.getText().toString();

        if (username.length() < 1 || password.length() < 1 || password2.length() < 1 || email.length() < 1) {
            Toast.makeText(getApplicationContext(), getText(R.string.toast_reg_req), Toast.LENGTH_LONG).show();
        }
        else if (!password.equals(password2)) {
            Log.e("Pass", password);
            Log.e("Pass2", password2);
            Toast.makeText(getApplicationContext(), getText(R.string.register_2pass), Toast.LENGTH_LONG).show();
        }
        else if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), getText(R.string.toast_reg_pass), Toast.LENGTH_LONG).show();
        }
        else {
            String pHash = Security.hashPassword(password, null);
            final String salt = username + ":" + pHash.split(":")[0];
            pHash = pHash.split(":")[1];

            User user = new User(username, email, pHash);
            if (imageuser != null) {
                String s = new String(imageuser, StandardCharsets.UTF_8);
                user.setImage(s);
            }

            Call<JsonObject> call = apiService.register(user);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getText(R.string.toast_reg_success), Toast.LENGTH_LONG).show();
                        Set<String> salts = sharedPreferences.getStringSet("salts", new ArraySet<String>());
                        salts.add(salt);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("salts", salts);
                        editor.apply();
                        RegisterActivity.super.finish();
                    } else {
                        Log.e("Register not successful", response.message());
                        if (response.code() == 409) {
                            Toast.makeText(getApplicationContext(), getText(R.string.toast_reg_fail), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.communicationError), Toast.LENGTH_LONG).show();
                    Log.e("FAILURE ON REGISTER", t.getMessage());
                }
            });
        }
    }

    public void deleteImage(View view) {
        ImageView imvw = findViewById(R.id.imageView);
        TextView tv = findViewById(R.id.preview);
        Button btn = findViewById(R.id.deletebtn);
        imvw.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        imageuser = null;
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap value = (Bitmap) extras.get("data");
            ImageView imvw = findViewById(R.id.imageView);
            TextView tv = findViewById(R.id.preview);
            imvw.setImageBitmap(Bitmap.createScaledBitmap(value, 200, 200, false));
            tv.setText(getString(R.string.preview));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageuser = stream.toByteArray();
            value.recycle();
            Button btn = findViewById(R.id.deletebtn);
            btn.setVisibility(View.VISIBLE);
            imvw.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }
    }
}