package com.example.practicacomov;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.example.practicacomov.models.Marker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private SharedPreferences sharedPreferences;
    private String user;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ClusterManager<Marker> clusterManager;
    private ClusterRenderer clusterRenderer;
    private LatLng messageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jojosapp", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("saveUser", false))
            user = sharedPreferences.getString("user", null);
        else
            user = getIntent().getExtras().getString("username");
        if (getIntent().getExtras().getBoolean("isFromMessage")) {
            Double latitude = new Double(getIntent().getExtras().getString("location").split("\n")[0]);
            Double longitude = new Double(getIntent().getExtras().getString("location").split("\n")[1]);
            messageLocation = new LatLng(latitude, longitude);
        } else messageLocation = null;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Este código no se puede refactorizar (aunque lo parezca) porque la otra parte se ejecuta en un listener asíncrono (el código se ejecuta solo cuándo se ha completado la operación de
        //obtención de la localización), y si se refactoriza, el código posterior al listener podría ejecutarse antes de que se complete, produciendo errores.
        if (messageLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(messageLocation.latitude - .1, messageLocation.longitude - .05), new LatLng(messageLocation.latitude + .1, messageLocation.longitude + .05)), 0));
            clusterManager = new ClusterManager<>(getApplicationContext(), map);
            clusterRenderer = new ClusterRenderer(getApplicationContext(), map, clusterManager);
            clusterManager.setRenderer(clusterRenderer);
            Marker userMarker =  new Marker(messageLocation, user, getString(R.string.map_snippet_text), R.drawable.jojo_user, null);
            clusterManager.addItem(userMarker);
            clusterManager.cluster();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            LatLng userLocation = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(userLocation.latitude-.1, userLocation.longitude-.05), new LatLng(userLocation.latitude+.1, userLocation.longitude+.1)), 0));
                            clusterManager = new ClusterManager<>(getApplicationContext(), map);
                            clusterRenderer = new ClusterRenderer(getApplicationContext(), map, clusterManager);
                            clusterManager.setRenderer(clusterRenderer);
                            Marker userMarker =  new Marker(userLocation, user, getString(R.string.map_snippet_text), R.drawable.jojo_user, null);
                            clusterManager.addItem(userMarker);
                            clusterManager.cluster();
                        }
                    }
                });
            }
        }

    }


}
