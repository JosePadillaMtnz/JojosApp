package com.example.practicacomov;

import android.app.Activity;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import com.example.practicacomov.models.ItemContactsView;
import com.example.practicacomov.models.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javax.crypto.ExemptionMechanismException;

public class FileManager extends AppCompatActivity {
    String delim = "-____-";
    String delimEnd = "_----_";
    String fileContacts = "contactsFile.txt";

    public void addContactFile(String image, String username, String realname) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(fileContacts, Activity.MODE_APPEND));
            archivo.write(image + delim + username + delim + realname + "\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {}
    }

    public ArrayList<ItemContactsView> getContacts() {
        ArrayList<ItemContactsView> items = new ArrayList<ItemContactsView>();
        try{
            InputStreamReader archivo = new InputStreamReader(openFileInput(fileContacts));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {
                String[] lin = linea.split(delim);
                int image;
                if(lin[0].equals("default")) image = R.drawable.jojo_user;
                else image = R.drawable.jojo_email;
                ItemContactsView item = new ItemContactsView(image, lin[1], lin[2]);
                items.add(item);
                linea = br.readLine();
            }

            Log.i("Contenido ->", items.toString());
            br.close();
            archivo.close();
        } catch (IOException e) {
        }
        return items;
    }

    public boolean existeArchivo(String archivos[], String name) {
        for (int i = 0; i < archivos.length; i++) {
            if (name.equals(archivos[i])) return true;
        }
        return false;
    }

    public void addMessageFile(Message message, String username, String filename) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(filename, Activity.MODE_APPEND));
            archivo.write(message.getBody() + "-----" + "[date]" + "\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {

        }
    }
    public void addMessageGroupFile(Message message, String username, String filename) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(filename, Activity.MODE_APPEND));
            archivo.write(username + "-----" + message.getBody() + "-----" + "[date]" + "\n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {

        }
    }

    public void getMessageFile(String filename) {
        String archivos[] = this.fileList();

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

    public void initiateFile(String filename) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(filename, Activity.MODE_APPEND));
            archivo.write("");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {}
    }



    ////////////////Secure part

    public void addContactSec(String image, String username, String realname) {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (Exception e) {e.printStackTrace();}

        try {
            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    new File(this.getFilesDir(), fileContacts),
                    getApplicationContext(),
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();

            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(encryptedFile), true));
            writer.write(image + delim + username + delim + realname + "\n");
        } catch (GeneralSecurityException gse) {
            // Error occurred getting or creating keyset.
        } catch (IOException ex) {
            Log.e("No existe el archivo", "ex.printStackTrace()");
        }
    }

    public ArrayList<ItemContactsView> getContactsSec() {
        ArrayList<ItemContactsView> items = new ArrayList<ItemContactsView>();
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (Exception e) { e.printStackTrace(); }

        ByteArrayOutputStream byteStream;
        try {
            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    new File(this.getFilesDir(), fileContacts),
                    getApplicationContext(),
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();

            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(encryptedFile)));

            String line = reader.readLine();
            while (line != null) {
                String[] linea = line.split(delim);
                int image;
                if(linea[0].equals("default")) image = R.drawable.jojo_user;
                else image = R.drawable.jojo_email;
                ItemContactsView item = new ItemContactsView(image, linea[1], linea[2]);
                items.add(item);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e("No existe el archivo", "Caranchao");
        }
        return items;
    }
}
/*
 public void addContactSec(String image, String username, String realname) {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (Exception e) {e.printStackTrace();}

        try {
            if (encryptedFile == null) {
                encryptedFile = new EncryptedFile.Builder(
                        new File(this.getFilesDir(), fileContacts),
                        this,
                        masterKeyAlias,
                        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build();
            }

            //BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(encryptedFile), true));
            //writer.write(image + delim + username + delim + realname + "\n");
            String data = image + delim + username + delim + realname + delimFinal;
            FileOutputStream output = encryptedFile.openFileOutput();
            output.write(data.getBytes());
            output.close();
        } catch (GeneralSecurityException gse) {
            Log.e("Fallo en securizar en add.", "Patata");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("Fallo al abrir el archivo en add.", "Si el archivo existe pero es un directorio en lugar de un archivo normal, no existe pero no se puede crear o no se puede abrir por ningún otro motivo");
        }
    }

    public ArrayList<ItemContactsView> getContactsSec() {
        ArrayList<ItemContactsView> items = new ArrayList<ItemContactsView>();
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (Exception e) { e.printStackTrace(); }

        ByteArrayOutputStream byteStream;
        try {
            if (encryptedFile == null) {
                encryptedFile = new EncryptedFile.Builder(
                        new File(this.getFilesDir(), fileContacts),
                        this,
                        masterKeyAlias,
                        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build();
            }
            /*
            Log.i("1", "Seguimos vivos");
            Log.i("1", encryptedFile.toString());
            Log.i("1", String.valueOf(encryptedFile));
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(encryptedFile)));
            Log.i("2", "Seguimos vivos");
            String line = reader.readLine();
            Log.i("3", "Seguimos vivos");
            while (line != null) {
                String[] linea = line.split(delim);
                int image;
                if(linea[0].equals("default")) image = R.drawable.jojo_user;
                else image = R.drawable.jojo_email;
                ItemContactsView item = new ItemContactsView(image, linea[1], linea[2]);
                items.add(item);
                line = reader.readLine();
            }

FileInputStream input = encryptedFile.openFileInput();
    byte sol[] = new byte[100];
            input.read(sol);
                    String leido = new String(sol, StandardCharsets.UTF_8);
                    Log.e("Leido", leido);
                    String[] cadaitem = leido.split(delimFinal);
                    Log.e("Tamaño lista items", new String(cadaitem.length+""));
                    for(int i = 0; i < (cadaitem.length); i++) {
        Log.e("Item", cadaitem[i]);
        }
        for(int i = 0; i < (cadaitem.length-1); i++) {
        String[] linea = cadaitem[i].split(delim);
        int image;
        if(linea[0].equals("default")) image = R.drawable.jojo_user;
        else image = R.drawable.jojo_email;
        ItemContactsView item = new ItemContactsView(image, linea[1], linea[2]);
        items.add(item);
        }
        } catch (GeneralSecurityException e) {
        Log.e("Fallo en securizar en get.", "Patata");
        } catch (IOException e) {
        Log.e("Fallo al abrir el archivo en get.", "Si el archivo existe pero es un directorio en lugar de un archivo normal, no existe pero no se puede crear o no se puede abrir por ningún otro motivo");
        }
        return items;
        }
 */