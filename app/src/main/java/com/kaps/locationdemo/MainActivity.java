package com.kaps.locationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView textViewAddress;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewAddress = findViewById(R.id.textViewAddress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
                        }
                    }
                }
                break;
        }
    }

    private void getAddress(Location location) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            if (address != null) {
                textViewAddress.setText("Address : " + address);
            }
            Log.d("Log", "address : " + address);
            Log.d("Log", "city  : " + city);
            Log.d("Log", "state  : " + state);
            Log.d("Log", "country  : " + country);
            Log.d("Log", "postalCode  : " + postalCode);
            Log.d("Log", "knownName  : " + knownName);
        } catch (IOException e) {
            Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Log", "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Lat :" + location.getLatitude() + "\nLng : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        getAddress(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
