package com.chaemil.gmapsapiexamples.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaemil.gmapsapiexamples.ExampleApp;
import com.chaemil.gmapsapiexamples.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private ExampleApp app;

    @Override
    protected void onResume() {
        super.onResume();
        app.setMapActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.setMapActivity(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        app = ((ExampleApp) getApplication());

        getUI();
    }

    private void getUI() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng milosovo = new LatLng(50.090265, 14.399087);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(milosovo, 17));
        map.addMarker(new MarkerOptions()
                .title("Milošovo")
                .snippet("Víte co je to pussy?")
                .position(milosovo));

        map.getUiSettings().setCompassEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(true);

        map.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(49.516773, 15.907910))  // Chata
                .add(new LatLng(50.090265, 14.399087))  // Milošovo
                .add(new LatLng(21.291, -157.821))  // Občas tam jezdim
                .add(new LatLng(37.423, -122.091))  // Big Brother
        ).setColor(R.color.colorAccent);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("marker.id", marker.getId());  //m0
                Log.d("marker.title", marker.getTitle()); //Milošovo
                Log.d("marker.snippet", marker.getSnippet());  //Víte co je to pussy?
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                RelativeLayout layout = new RelativeLayout(getApplicationContext());

                if (marker.getSnippet() != null) {
                    TextView name = new TextView(getApplicationContext());
                    name.setText(marker.getTitle());
                    name.setTextColor(getResources().getColor(R.color.white));
                    layout.addView(name);
                    layout.setBackgroundColor(getResources().getColor(R.color.black));
                    layout.setPadding(20, 20, 20, 20);
                }

                return layout;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    @Override
    public void userLocationUpdated(Location location) {
        super.userLocationUpdated(location);

        Log.d("userLocationUpdated", location.toString());
    }
}
