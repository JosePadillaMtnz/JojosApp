package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;

public class AddContactActivity extends AppCompatActivity {
    private static String authToken;
    private static APIService apiService;
    private static String username;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private String delim = "--__--";
    private String fileContacts = "ContactsList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

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
        authToken = sharedPreferences.getString("authToken", null);
        username = sharedPreferences.getString("username", null);
    }

    public void addContact(View view) {
        TextView nameuser = findViewById(R.id.textUserName);
        TextView namereal = findViewById(R.id.textNameReal);
        String user = nameuser.getText().toString();
        String real = namereal.getText().toString();
        if (user.length() < 1 || real.length() < 1) {
            Toast.makeText(getApplicationContext(), getString(R.string.addContactFail), Toast.LENGTH_LONG).show();
        }
        else {
            addContactFile("default", user, real);
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        }
    }

    public void addContactFile(String image, String username, String realname) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(fileContacts, Activity.MODE_APPEND));
            archivo.write(image + delim + username + delim + realname + "\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {}
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}