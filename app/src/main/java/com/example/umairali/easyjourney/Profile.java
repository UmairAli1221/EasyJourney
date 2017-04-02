package com.example.umairali.easyjourney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private ListView mUser;
    private TextView FirstName,LastName,Email,Gender,PhoneNumber,Adress;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final ArrayAdapter<String> arrayAdapter;
        FirstName=(TextView)findViewById(R.id.firestname);
        LastName=(TextView)findViewById(R.id.lastname);
        Email=(TextView)findViewById(R.id.email);
        Gender=(TextView)findViewById(R.id.gender);
        PhoneNumber=(TextView)findViewById(R.id.phoneNo);
        Adress=(TextView)findViewById(R.id.address);
        SharedPreferences sharedRef= getSharedPreferences("UserId", Context.MODE_PRIVATE);
        String markerTitle=sharedRef.getString("id","");
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mDatabaseUsers.child(markerTitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Retrival retrival=dataSnapshot.getValue(Retrival.class);
                String firstname=retrival.getFirstname();
                FirstName.setText(firstname);
                String lastname=retrival.getLastname();
                LastName.setText(lastname);
                String email=retrival.getEmail();
                Email.setText(email);
                String gender=retrival.getGender();
                Gender.setText(gender);
                String phonenumber=retrival.getPhone();
                PhoneNumber.setText(phonenumber);
                String address=retrival.getAddress();
                Adress.setText(address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
