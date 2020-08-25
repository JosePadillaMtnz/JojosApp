package com.example.practicacomov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicacomov.interfaces.APIService;
import com.example.practicacomov.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.nio.channels.InterruptedByTimeoutException;
import java.security.GeneralSecurityException;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static String authToken;
    private static APIService apiService;
    private static String username;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private boolean mapPermission = false;
    private static final int PERMISSION_REQUEST_ENABLE_GPS = 1000;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1001;
    private static final int ERROR_DIALOG_REQUEST = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        boolean savedCredentials = sharedPreferences.getBoolean("saveUser", false);
        if (savedCredentials) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_autologin), Toast.LENGTH_LONG).show();
            CountDownTimer timer = new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            }.start();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void recoverMessages(View view) {
        Intent intent = new Intent(this, RecoverMessagesActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("authToken", authToken);
        startActivity(intent);
    }

    public void login(String name, final String pHash) {
        username = name;
        final User user = new User(name, null, pHash);
        Call<JsonPrimitive> call = apiService.login(user);
        call.enqueue(new Callback<JsonPrimitive>() {
            @Override
            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                if (response.isSuccessful()) {
                    authToken = response.body().toString();
                    editor.putString("authToken", authToken);
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    CheckBox checkBox = findViewById(R.id.checkSaveCredentials);
                    if (checkBox.isChecked()) {
                        editor.putBoolean("saveUser", true);
                        editor.putString("user", username);
                        editor.putString("pHash", pHash);
                    } else {
                        editor.putBoolean("saveUser", false);
                        intent.putExtra("username", username);
                    }
                    editor.apply();
                    startActivity(intent);
                } else {
                    authToken = null;
                    Toast.makeText(getApplicationContext(), getText(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                authToken = null;
                Toast.makeText(getApplicationContext(), getText(R.string.toast_login_failure), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginAction(View view) {
        TextView loginText = findViewById(R.id.loginText);
        TextView passText = findViewById(R.id.passwordText);
        username = loginText.getText().toString();
        String salt = null;
        Set<String> salts = sharedPreferences.getStringSet("salts", null);
        if (salts != null) {
            for (String string : salts) {
                if (string.split(":")[0].equals(username)) {
                    salt = string.split(":")[1];
                    break;
                }
            }
        }
        if (salt != null) {
            String pHash = Security.hashPassword(passText.getText().toString(), Security.fromHex(salt));
            pHash = pHash.split(":")[1];
            login(username, pHash);
            /*
            editor.remove("salts");
            editor.apply();*/
        }
        else Toast.makeText(getApplicationContext(), "This user was not registered in this device", Toast.LENGTH_LONG).show();

    }

    public void enviomensaje(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void map(View view) {
        if (checkMapServices())
            if (sharedPreferences.getBoolean("userSave", false))
                startActivity(new Intent(this, MapActivity.class));
            else {
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("isFromMessage", false);
                startActivity(intent);
            }
    }

    private boolean checkMapServices(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS){
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.alert_gps_use))
                        .setPositiveButton(getString(R.string.alert_gps_positive_answer), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), PERMISSION_REQUEST_ENABLE_GPS);
                            }
                        })
                        .setNegativeButton(getString(R.string.alert_gps_negative_answer), null);
                builder.create().show();
                return false;
            }
            return true;
        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.google_api_availability_error), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] results) {
        mapPermission = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED)
                    mapPermission = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_ENABLE_GPS) {
            if (mapPermission){
                map(findViewById(R.id.mainLayout));
            }
            else {
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mapPermission = true;
                    map(findViewById(R.id.mainLayout));
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        }
    }
}

