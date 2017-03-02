package com.example.umairali.easyjourney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat=0.0,log=0.0;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(MapsActivity.this, Login.class);
            startActivity(intent);
        } else {
            mUserId = mFirebaseUser.getUid();
            mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);
            GeoFire geoFire = new GeoFire(mDatabaseUsers);
            geoFire.getLocation("Locations", new com.firebase.geofire.LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    if (location != null) {
                        Toast.makeText(MapsActivity.this, "The location for key %s is [%f,%f]", Toast.LENGTH_LONG).show();
                        // System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                        lat = location.latitude;
                        log = location.longitude;
                        LatLng sydney = new LatLng(lat,log);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    } else {
                        Toast.makeText(MapsActivity.this, "There is no location for key %s in GeoFire", Toast.LENGTH_LONG).show();
                        // System.out.println(String.format("There is no location for key %s in GeoFire", key));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MapsActivity.this, "There was an error getting the GeoFire location:", Toast.LENGTH_LONG).show();
                    //System.err.println("There was an error getting the GeoFire location: " + databaseError);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
