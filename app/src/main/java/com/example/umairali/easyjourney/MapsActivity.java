package com.example.umairali.easyjourney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabaseUsers,mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private  double dlat=0.0,dlog=0.0;
    private  double lat=0.0,log=0.0;
    private Map<String,Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LatLng start = new LatLng(dlat,dlog);
        markers = new HashMap<String, Marker>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(MapsActivity.this, Login.class);
            startActivity(intent);
        } else {
            mUserId = mFirebaseUser.getUid();
            mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child("StartPoint");
            final GeoFire geoFire = new GeoFire(mDatabaseUsers);
            geoFire.getLocation(mUserId, new com.firebase.geofire.LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    if (location != null) {
                        Toast.makeText(MapsActivity.this, "The Start Point:", Toast.LENGTH_LONG).show();
                        dlat = location.latitude;
                        dlog = location.longitude;
                        LatLng start = new LatLng(dlat,dlog);
                        mMap.addMarker(new MarkerOptions().position(start).title("Start Marker"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                        GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(dlat,dlog),500);
                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(String key, GeoLocation location) {

                                    Toast.makeText(MapsActivity.this, "StartPoint Found", Toast.LENGTH_SHORT).show();
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
                                    markers.put(key, marker);

                                    CheckDestination();

                            }
                            @Override
                            public void onKeyExited(String key) {
                                Marker marker = markers.get(key);
                                if (marker != null) {
                                    marker.remove();
                                    markers.remove(key);
                                }
                                Toast.makeText(MapsActivity.this, " Startpoint left the place", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onKeyMoved(String key, GeoLocation location) {
                                Marker marker = markers.get(key);
                                if (marker != null) {
                                }
                                Toast.makeText(MapsActivity.this, "key moved but here", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onGeoQueryReady() {
                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {
                                Toast.makeText(MapsActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(MapsActivity.this, "There is no location for key %s in GeoFire", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MapsActivity.this, "There was an error getting the GeoFire location:", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void CheckDestination() {
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child("Destination");
        final GeoFire geoFire1 = new GeoFire(mDatabaseUsers);
        geoFire1.getLocation(mUserId, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    Toast.makeText(MapsActivity.this, "The Destination Point", Toast.LENGTH_LONG).show();
                    lat = location.latitude;
                    log = location.longitude;
                    final LatLng end = new LatLng(lat,log);
                    mMap.addMarker(new MarkerOptions().position(end).title("Destination Marker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(end));
                    GeoQuery geoQuery=geoFire1.queryAtLocation(new GeoLocation(lat,log),500);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            Toast.makeText(MapsActivity.this, "Destination Found", Toast.LENGTH_SHORT).show();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
                            markers.put(key, marker);
                        }
                        @Override
                        public void onKeyExited(String key) {
                            Marker marker = markers.get(key);
                            if (marker != null) {
                                marker.remove();
                                markers.remove(key);
                            }
                            Toast.makeText(MapsActivity.this, "Destination left the place", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {
                            Marker marker = markers.get(key);
                            if (marker != null) {
                            }
                            Toast.makeText(MapsActivity.this, "key moved but here", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onGeoQueryReady() {
                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {
                            Toast.makeText(MapsActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MapsActivity.this, "There is no location for key %s in GeoFire", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "There was an error getting the GeoFire location:", Toast.LENGTH_LONG).show();
            }
        });
    }
}
