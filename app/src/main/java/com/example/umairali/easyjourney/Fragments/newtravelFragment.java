package com.example.umairali.easyjourney.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umairali.easyjourney.Login;
import com.example.umairali.easyjourney.MapsActivity;
import com.example.umairali.easyjourney.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by umair.ali on 11/2/2016.
 */

public class newtravelFragment extends Fragment {
    EditText time,date,Startpoint,EndPoint;
    Context context;
    Button btn;
    View myView;
    String lt,lg;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseUsers;
    private String mUserId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newtravell, container, false);
        context = myView.getContext();
        time=(EditText)myView.findViewById(R.id.time);
        date=(EditText)myView.findViewById(R.id.date);
        EndPoint=(EditText)myView.findViewById(R.id.epoint);
        btn = (Button) myView.findViewById(R.id.searchbtn);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent=new Intent(context, Login.class);
            startActivity(intent);
        } else {
            mUserId = mFirebaseUser.getUid();
            mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);
            final GeoFire geoFire=new GeoFire(mDatabaseUsers);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Startpoint = (EditText) myView.findViewById(R.id.spoint);
                final String Startaddress = Startpoint.getText().toString();
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List addressList = geocoder.getFromLocationName(Startaddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        lt = String.valueOf(address.getLatitude());
                        lg = String.valueOf(address.getLongitude());
                        geoFire.setLocation("Locations", new GeoLocation(address.getLatitude(), address.getLongitude())
                                , new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                        if (error != null) {
                                            Toast.makeText(context, "There was an error saving the location to GeoFire: ", Toast.LENGTH_LONG).show();
                                        } else {
                                            Intent i = new Intent(context, MapsActivity.class);
                                            startActivity(i);
                                            Toast.makeText(context, "\"Location saved on server successfully!\"", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        }
        return myView;
    }
}
