package com.example.practicacomov;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practicacomov.models.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class ClusterRenderer extends DefaultClusterRenderer<Marker> {
    private IconGenerator iconGenerator;
    private ImageView imageView;
    private static int markerSize = 100;
    private static int markerPadding = 2;

    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<Marker> clusterManager) {
        super(context, map, clusterManager);

        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerSize, markerSize));
        imageView.setPadding(markerPadding, markerPadding, markerPadding, markerPadding);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(Marker item, MarkerOptions markerOptions) {
        imageView.setImageResource(item.getIcon());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).title(item.getTitle());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Marker> cluster) {
        return false;
    }
}