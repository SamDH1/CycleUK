package com.pul.sam.Cycle;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.deps.dagger.internal.DoubleCheckLazy;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.cycleuk.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    TextView lonText;
    TextView latText;
    TextView lonMin;
    TextView lonMax;
    TextView latMin;
    TextView latMax;
    private static double LAT_MIN = 0;
    private static double LAT_MAX = 0;
    private static double LON_MIN = 0;
    private static double LON_MAX = 0;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static Double myLatitude;
    private static Double myLongitude;
    private static final int MY_PERMISSION_REQUESTS_FINE_LOCATION = 101;
    private boolean permissionIsGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recList = (RecyclerView) findViewById(R.id.recyclerView);
        recList.setHasFixedSize(true);

        latText = (TextView) findViewById(R.id.latTest);
        lonText = (TextView) findViewById(R.id.lonTest);
        latMax = (TextView) findViewById(R.id.latMax);
        latMin = (TextView) findViewById(R.id.latMin);;
        lonMax = (TextView) findViewById(R.id.lonMax);;
        lonMin = (TextView) findViewById(R.id.lonMin);;

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(6 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUESTS_FINE_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        latText.setText("Lat : " + String.valueOf(myLatitude));
        lonText.setText("Lon : " + String.valueOf(myLongitude));

        LAT_MAX = myLatitude + (0.0144927 * 20);
        LAT_MIN = myLatitude - (0.0144927 * 20);
        LON_MAX = myLongitude + (0.0181818 * 20);
        LON_MIN = myLongitude - (0.0181818 * 20);

        latMax.setText("Lat Max : " + String.valueOf(LAT_MAX));
        latMin.setText("Lat Min : " + String.valueOf(LAT_MIN));

        lonMax.setText("Lon Max : " + String.valueOf(LON_MAX));
        lonMin.setText("Lon Min : " + String.valueOf(LON_MIN));





    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionIsGranted) {
            if (googleApiClient.isConnected()) {
                requestLocationUpdates();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (permissionIsGranted)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (permissionIsGranted)
            googleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUESTS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true;
                } else {
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This application requires location permission to work properly", Toast.LENGTH_SHORT).show();
                    //Create a funtion here that reminds the user the app won't work without the location
                }
                break;
        }
    }

    public static class CycleTrails {

        static class Trails {
            Trails(double lat, double lon, String name){
                this.lat = lat;
                this.lon = lon;
                this.name = name;
            }

            String name;
            double lat;
            double lon;

        }


        public static ArrayList<Trails> getTrails() {
            ArrayList<Trails> trailList = new ArrayList<>();
            Trails trail = new Trails(51.7181283, -3.3633637, "Bike Park Wales");
            trailList.add(trail);

            ArrayList<Trails> localTrails = new ArrayList<>();
            for(Trails trails : trailList){
                if(trails.lat > LAT_MIN && trails.lat < LAT_MAX && trails.lon < LON_MIN && trails.lon > LON_MAX) {
                    localTrails.add(trails);
                }
            }
            return localTrails;
        }

    }







    /*public class weather {
        GET https://api.darksky.net/forecast/ddcf14967352486713258d18d075f917/[latitude],[longitude] {

    } */

}




