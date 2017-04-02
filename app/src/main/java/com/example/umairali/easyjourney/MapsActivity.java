package com.example.umairali.easyjourney;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
    private String serverKey ="AIzaSyAVNeFDVwi62iTlz2B5fBiw1d9I7zMziPg";
    private DatabaseReference mDatabaseUsers,mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;
    private  double dlat=0.0,dlog=0.0;
    private  double lat=0.0,log=0.0;
    private Map<String,Marker> markers;
    private Map<String,Marker> markers1;
    private double latitude;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new HashMap<String, Marker>();
        markers1 = new HashMap<String, Marker>();
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
            getLocation();
        }
    }
    private void getLocation(){
        mUserId = mFirebaseUser.getUid();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child("StartPoint");
        final GeoFire geoFire = new GeoFire(mDatabaseUsers);

        geoFire.getLocation(mUserId, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    Toast.makeText(MapsActivity.this, "The Start Point:", Toast.LENGTH_LONG).show();
                    lat = location.latitude;
                    log = location.longitude;
                    Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("My Start"));
                    marker.setTag(key);
                    final LatLng start = new LatLng(lat,log);
                    GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(lat,log),200);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(final String key, GeoLocation location) {
                            Toast.makeText(MapsActivity.this, " Start key Found"+key, Toast.LENGTH_SHORT).show();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Start Of other"));
                            marker.setTag(key);
                            markers.put(key, marker);
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent1 = new Intent(MapsActivity.this, Profile.class);
                                        SharedPreferences sharedRef= getSharedPreferences("UserId", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedRef.edit();
                                        editor.putString("id",(marker.getTag().toString()));
                                        editor.apply();
                                        startActivity(intent1);
                                    }
                                });
                            destination(key);
                        }
                        @Override
                        public void onKeyExited(String key) {
                            Marker marker = markers.get(key);
                            Marker marker1=markers1.get(key);
                            if (marker != null) {
                                marker.remove();
                                markers.remove(key);
                                /*if (marker1 != null) {
                                    marker1.remove();
                                    markers1.remove(key);
                                    Toast.makeText(MapsActivity.this, "key remove" + key, Toast.LENGTH_SHORT).show();
                                }*/
                            }
                            Toast.makeText(MapsActivity.this, "key remove"+key, Toast.LENGTH_SHORT).show();
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
    public void destination(String key1){
        Toast.makeText(MapsActivity.this, "Error Occured"+latitude, Toast.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Destination");
        final GeoFire geoFire1 = new GeoFire(mDatabase);
        geoFire1.getLocation(mUserId, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(final String key, GeoLocation location) {
                if (location != null) {
                    Toast.makeText(MapsActivity.this, "The Destination Point", Toast.LENGTH_LONG).show();
                    dlat = location.latitude;
                    dlog = location.longitude;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("My dest"));
                    final LatLng endpoint = new LatLng(dlat,dlog);
                    GeoQuery geoQuery=geoFire1.queryAtLocation(new GeoLocation(lat,log),200);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            Toast.makeText(MapsActivity.this, " Destination key Found", Toast.LENGTH_SHORT).show();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Destination of other"));
                            markers1.put(key, marker);
                        }
                        @Override
                        public void onKeyExited(String key) {
                            Marker marker = markers1.get(key);
                            if (marker != null) {
                                marker.remove();
                                markers1.remove(key);
                            }
                            Toast.makeText(MapsActivity.this, "key left the place", Toast.LENGTH_SHORT).show();
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
                               /* GoogleDirection.withServerKey(serverKey).from(start).to(endpoint).transportMode(TransportMode.DRIVING)
                                        .language(Language.ENGLISH)
                                        .execute(new DirectionCallback() {
                                    @Override
                                    public void onDirectionSuccess(Direction direction, String rawBody) {
                                        Toast.makeText(MapsActivity.this, "Success with status : " + direction.getStatus(),Toast.LENGTH_SHORT).show();
                                        if (direction.isOK()) {
                                            mMap.addMarker(new MarkerOptions().position(start).title("Start Marker"));
                                            mMap.addMarker(new MarkerOptions().position(endpoint).title("Destination Marker"));

                                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                            mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));
                                        }
                                        }
                                    @Override
                                    public void onDirectionFailure(Throwable t) {

                                    }
                                });*/
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
//Toast.makeText(MapsActivity.this, "origine "+directions.getSlatitude(), Toast.LENGTH_SHORT).show();
                               /* Directions directionOrigine=new Directions(oriLat,oriLong);
                                if(directions.getSlatitude()!=0&&directions.getSlongitude()!=0&&destination.getDlatitude()!=0&&destination.getDlongitude()!=0){
                                    olat=directionOrigine.getSlatitude();
                                    olong=directionOrigine.getSlongitude();
                                    dlati=destination.getDlatitude();
                                    dlong=destination.getDlongitude();
                                    final LatLng s=new LatLng(olat,olong);
                                    final LatLng o=new LatLng(dlat,dlong);
                                    GoogleDirection.withServerKey(serverKey).from(s).to(o).transportMode(TransportMode.DRIVING)
                                            .language(Language.ENGLISH)
                                            .execute(new DirectionCallback() {
                                                @Override
                                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                                    Toast.makeText(MapsActivity.this, "Success with status : " + direction.getStatus(),Toast.LENGTH_SHORT).show();
                                                    if (direction.isOK()) {
                                                        mMap.addMarker(new MarkerOptions().position(s));
                                                        mMap.addMarker(new MarkerOptions().position(o));
                                                        ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                                        mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED));
                                                    }
                                                }
                                                @Override
                                                public void onDirectionFailure(Throwable t) {
                                                }
                                            });
                                }else {
                                    Toast.makeText(MapsActivity.this, "Nothing is near", Toast.LENGTH_LONG).show();
                                }*/