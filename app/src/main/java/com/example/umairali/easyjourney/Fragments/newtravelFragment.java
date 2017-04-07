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
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startPoint();
                }
            });
        }
        return myView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // showDatePicker();
            }
        });}

   /* private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            date.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
        }
    };*/

    private void startPoint() {
        Startpoint = (EditText) myView.findViewById(R.id.spoint);
        final String Startaddress = Startpoint.getText().toString();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child("StartPoint");
        final GeoFire geoFire=new GeoFire(mDatabaseUsers);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(Startaddress, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                lt = String.valueOf(address.getLatitude());
                lg = String.valueOf(address.getLongitude());
                geoFire.setLocation(mUserId, new GeoLocation(address.getLatitude(), address.getLongitude())
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    Toast.makeText(context, "There was an error saving the location to GeoFire: ", Toast.LENGTH_LONG).show();
                                } else {
                                    endPoint();
                                }
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }private void endPoint() {
        EndPoint=(EditText)myView.findViewById(R.id.endpoint);
        final String Destinationaddress = EndPoint.getText().toString();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users").child("Destination");
        final GeoFire geoFire2=new GeoFire(mDatabaseUsers);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(Destinationaddress, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                lt = String.valueOf(address.getLatitude());
                lg = String.valueOf(address.getLongitude());
                geoFire2.setLocation(mUserId, new GeoLocation(address.getLatitude(), address.getLongitude())
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    Toast.makeText(context, "There was an error saving the location to GeoFire: ", Toast.LENGTH_LONG).show();
                                } else {
                                    Intent i = new Intent(context, MapsActivity.class);
                                    startActivity(i);

                                }
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener ondateSet;
        private int year, month, day;

        public DatePickerFragment() {}

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        @SuppressLint("NewApi")
        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
             DatePickerDialog dialoge=new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            long now = System.currentTimeMillis() - 1000;
            dialoge.getDatePicker().setMinDate(now);
            return dialoge;
        }
    }*/
}