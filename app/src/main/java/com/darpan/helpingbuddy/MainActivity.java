package com.darpan.helpingbuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    Button button1, button2, button3, button4;
    public String Address ;
    public static String emailid = " ";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    Double lat1, long1;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String police = "100";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",police,null));
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ambulance = "108";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",ambulance,null));
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fire = "101";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",fire,null));
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String subject = "Emergency mail";
                    String body = "Hey! I need your help. It's an emergency. Please contact me ASAP!\n\n" +
                            "My location is: "+Address+"\n\nLatitude: "+lat1+"\n\nLongitude: "+long1;
                        if(SettingsMenu.changedmailID.length() > 0) {
                            emailid = SettingsMenu.changedmailID;
                            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                            startActivity(Intent.createChooser(emailIntent, "Sending email..."));
                        }
                        else {
                        Toast.makeText(getApplicationContext(),"Go to settings. Enter a mail ID first",Toast.LENGTH_LONG).show();
                    }
                } catch (Throwable throwable){
                    Toast.makeText(getApplicationContext(),"Request failed" + throwable.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //googleMap.setMyLocationEnabled(true);
                        Criteria criteria = new Criteria();
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        String provider = locationManager.getBestProvider(criteria, false);
                        Location location = locationManager.getLastKnownLocation(provider);
                        double lat =  location.getLatitude();
                        lat1 = lat;
                        double lng = location.getLongitude();
                        long1 = lng;
                        LatLng coordinate = new LatLng(lat, lng);
                        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                        StringBuilder builder = new StringBuilder();
                        try {
                            List<android.location.Address> address = geoCoder.getFromLocation(lat, lng, 1);
                            int maxLines = address.get(0).getMaxAddressLineIndex();
                            for (int i=0; i<maxLines; i++) {
                                String addressStr = address.get(0).getAddressLine(i);
                                builder.append(addressStr);
                                builder.append(" ");
                            }

                            Address = builder.toString();

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"Error1: "+e,Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(),"Error2: "+e,Toast.LENGTH_SHORT).show();
                        }if(googleMap != null){
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 14.5f));
                            Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinate).title(Address));
                            Toast.makeText(getApplicationContext(),"Latitude:"+lat+"\nLongitude:"+lng,Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Location couldn't be traced.", Toast.LENGTH_LONG).show();
                    }
                    return ;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                    setting();
                return true;
            case R.id.menu_about:
                Toast.makeText(getApplicationContext(), "Helping Buddy is developed by Darpan.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void setting(){
        Intent intent = new Intent(MainActivity.this, SettingsMenu.class);
        startActivity(intent);
    }
}

