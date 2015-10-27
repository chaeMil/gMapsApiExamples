package com.chaemil.gmapsapiexamples.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaemil.gmapsapiexamples.ExampleApp;
import com.chaemil.gmapsapiexamples.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/**
 * Created by chaemil on 23.10.15.
 */
public class StreetViewActivity extends BaseActivity implements OnStreetViewPanoramaReadyCallback {

    private StreetViewPanoramaFragment streetViewPanoramaFragment;
    private ExampleApp app;

    @Override
    protected void onPause() {
        super.onPause();
        app.setStreetViewActivity(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.setStreetViewActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view_activity);

        app = ((ExampleApp) getApplication());

        getUI();
    }

    private void getUI() {
        streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        panorama.setPosition(new LatLng(50.090265, 14.399087));
        panorama.orientationToPoint(new StreetViewPanoramaOrientation((float) 12.208492, (float) 122.4422));

        panorama.setOnStreetViewPanoramaCameraChangeListener(new StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener() {
            @Override
            public void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera camera) {
                Log.d("orientation", "tilt: " + camera.tilt + "bearing: " + camera.bearing);
            }
        });
    }
}
