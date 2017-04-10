package com.example.umairali.easyjourney.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.umairali.easyjourney.Login;
import com.example.umairali.easyjourney.R;
import com.example.umairali.easyjourney.Retrival;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Button btn;
    View myView;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.account,container,false);
        context = myView.getContext();
        Firstname=(TextView)myView.findViewById(R.id.profile);
        Firstname=(TextView)myView.findViewById(R.id.firstnameAccount);
        Lastname=(TextView)myView.findViewById(R.id.lastnameAccount);
        Email =(TextView)myView.findViewById(R.id.emailAccount);
        Gender=(TextView)myView.findViewById(R.id.genderAccount);
        Dateofbirth=(TextView)myView.findViewById(R.id.DOBAccount);
        Address=(TextView)myView.findViewById(R.id.addressAccount);
        Phone=(TextView)myView.findViewById(R.id.phoneAccount);
        Cnic=(TextView)myView.findViewById(R.id.cnicAccount);
        Licence_number=(TextView)myView.findViewById(R.id.licenceAccount);
        Registration_number=(TextView)myView.findViewById(R.id.Vehical_RegAccount);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent=new Intent(context, Login.class);
            startActivity(intent);
        } else {
            mUserId = mFirebaseUser.getUid();
            mDatabaseUsers.child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        Retrival retrival=dataSnapshot.getValue(Retrival.class);
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
                        String cnicnumber=retrival.getCnic();
                        Cnic.setText("Cnic Number:"+cnicnumber);
                        String phone=retrival.getPhone();
                        Phone.setText("Phone Number:"+phone);
                        String licence=retrival.getLicence_number();
                        String vrnumber=retrival.getRegistration_number();
                        if (licence!=null&&vrnumber!=null)
                        {
                            Licence_number.setText("Licence Number:"+licence);
                            Registration_number.setText("Vehical Registration Number:"+vrnumber);
                        }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return myView;
    }
}
