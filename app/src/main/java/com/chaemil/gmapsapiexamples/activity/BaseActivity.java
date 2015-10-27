package com.chaemil.gmapsapiexamples.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaemil.gmapsapiexamples.ExampleApp;

/**
 * Created by chaemil on 23.10.15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BaseActivity", "onResume");
        ((ExampleApp) getApplication()).activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("BaseActivity", "onPause");
        ((ExampleApp) getApplication()).activityPaused();
    }

    public void userLocationUpdated(Location location) {
        Log.d("userLocationUpdated", String.valueOf(location));
    }

}
