package com.chaemil.gmapsapiexamples.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chaemil.gmapsapiexamples.ExampleApp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by chaemil on 25.10.15.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int HEARTBEAT = 3 * 1000;
    public static final int LOCATION_INTERVAL = 800;
    public static final int LOCATION_FASTEST_INTERVAL = 400;

    private GoogleApiClient mGoogleApiClient;
    private ExampleApp app;
    private LocationRequest mLocationRequest;
    private boolean backgroundUpdate = true;
    private boolean receivingUpdates = false;

    @Override
    public void onCreate() {
        super.onCreate();

        app = (ExampleApp) getApplication();

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{

                    if (((ExampleApp) getApplication()).isActivityVisible()) {

                        if (!receivingUpdates) {
                            receivingUpdates = true;
                            startLocationUpdates();
                        }

                        createLocationRequest(LOCATION_INTERVAL,
                                LOCATION_FASTEST_INTERVAL,
                                LocationRequest.PRIORITY_HIGH_ACCURACY);

                    } else {
                        stopLocationUpdates();
                    }
                }
                catch (Exception e) {
                   Log.e("locationService", e.toString());
                }
                finally{
                    //also call the same runnable
                    handler.postDelayed(this, HEARTBEAT);
                }
            }
        };
        handler.postDelayed(runnable, HEARTBEAT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        restartService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        restartService();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (((ExampleApp) getApplication()).isActivityVisible()) {
            app.setUserLocation(location);
        }

        if (!backgroundUpdate) {
            stopLocationUpdates();
            backgroundUpdate = true;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("googleApiClient", "connected");

        createLocationRequest(LOCATION_INTERVAL,
                LOCATION_FASTEST_INTERVAL,
                LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("googleApiClient", "suspended");
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("googleApiClient", connectionResult.getErrorMessage());
        stopLocationUpdates();
    }

    private void restartService() {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed by android

        AlarmManager alarmService = (AlarmManager)getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);

        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 100, restartServicePI);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest(int interval, int fastestInterval, int accuracy) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(accuracy);
    }

    protected void startLocationUpdates() {
        receivingUpdates = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        receivingUpdates = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}
