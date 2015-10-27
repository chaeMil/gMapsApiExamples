package com.chaemil.gmapsapiexamples;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.chaemil.gmapsapiexamples.activity.MapActivity;
import com.chaemil.gmapsapiexamples.activity.StreetViewActivity;
import com.chaemil.gmapsapiexamples.service.LocationService;

/**
 * Created by chaemil on 25.10.15.
 */
public class ExampleApp extends Application {

    private Location userLocation;
    private boolean activityVisible;
    private Intent locationServiceIntent;
    private ServiceConnection locationService;
    private MapActivity mapActivity;
    private StreetViewActivity streetViewActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        locationServiceIntent = new Intent(this, LocationService.class);
        startService(locationServiceIntent);
        bindService(locationServiceIntent, locationServiceConnection, BIND_AUTO_CREATE);
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;

        if (mapActivity != null) {
            mapActivity.userLocationUpdated(userLocation);
        }

        if (streetViewActivity != null) {
            streetViewActivity.userLocationUpdated(userLocation);
        }
    }

    public boolean isActivityVisible() {
        return activityVisible;
    }

    public void activityResumed() {
        activityVisible = true;
    }

    public void activityPaused() {
        activityVisible = false;
    }

    public void setMapActivity(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    public void setStreetViewActivity(StreetViewActivity streetViewActivity) {
        this.streetViewActivity = streetViewActivity;
    }

    private ServiceConnection locationServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            locationService = (ServiceConnection) ((LocationService.MyBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            locationService = null;
        }
    };
}
