package com.example.umairali.easyjourney.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.umairali.easyjourney.Login;
import com.example.umairali.easyjourney.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by umair.ali on 11/2/2016.
 */

public class AccountFragment extends Fragment {
    private TextView Firstname;
    private TextView Lastname,Email,Gender, Dateofbirth, Address,Phone, Cnic, Licence_number, Registration_number;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseUsers;
    private String mUserId;
   private ListView mUserList;
   private ArrayList<String> mUsernames=new ArrayList<>();
    private Button btn;
    View myView;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.account,container,false);
        context = myView.getContext();
       /* Firstname=(TextView)myView.findViewById(R.id.firstname);
        Lastname=(TextView)myView.findViewById(R.id.lastname);
        Email =(TextView)myView.findViewById(R.id.email);
        Gender=(TextView)myView.findViewById(R.id.gender);
        Dateofbirth=(TextView)myView.findViewById(R.id.dateofbirth);
        Address=(TextView)myView.findViewById(R.id.address);
        Phone=(TextView)myView.findViewById(R.id.phoneNumber);
        Cnic=(TextView)myView.findViewById(R.id.cnicinumber);
        Licence_number=(TextView)myView.findViewById(R.id.licencenumber);
        Registration_number=(TextView)myView.findViewById(R.id.vehicalRegistrationNumber);*/
        mUserList=(ListView)myView.findViewById(R.id.myList);
        final ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,mUsernames);
        mUserList.setAdapter(arrayAdapter);
        /*btn=(Button)myView.findViewById(R.id.editProfile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Settings.class));
            }
        });*/
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent=new Intent(context, Login.class);
            startActivity(intent);
        } else {
           mUserId = mFirebaseUser.getUid();
            mDatabaseUsers.child(mUserId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String vlues=dataSnapshot.getValue(String.class);
                    mUsernames.add(vlues);
                    arrayAdapter.notifyDataSetChanged();
            }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }});

           /* mUserId = mFirebaseUser.getUid();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        Retrival retrival=postSnapshot.getValue(Retrival.class);
                        String firstname=retrival.getFirstname();
                        Firstname.setText(firstname);
                        String lastname=retrival.getLastname();
                        Lastname.setText(lastname);
                        String email=retrival.getEmail();
                        Email.setText("Email:"+email);
                        String gender=retrival.getGender();
                        Gender.setText("Gender:"+gender);
                        String dateofbirth=retrival.getDateofbirth();
                        Dateofbirth.setText("Date of birth:"+dateofbirth);
                        String address=retrival.getAddress();
                        Address.setText("Address:"+address);
                        int cnicnumber=retrival.getCnic();
                        Cnic.setText("Cnic Number:"+cnicnumber);
                        int phone=retrival.getPhone();
                        Phone.setText("Phone Number:"+phone);
                        int licence=retrival.getLicence_number();
                        int vrnumber=retrival.getRegistration_number();
                        if (licence!=0&&vrnumber!=0)
                        {
                            Licence_number.setText("Licence Number:"+licence);
                            Registration_number.setText("Vehical Registration Number:"+vrnumber);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }
        return myView;
    }
}
