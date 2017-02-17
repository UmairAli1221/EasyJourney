package com.example.umairali.easyjourney;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umairali.easyjourney.images.ProfileImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.umairali.easyjourney.R.id.firstnamebtn;

public class Traveller extends AppCompatActivity {
    Button btn;
    EditText firstname, lastname, Email, password, retype_password, phone, cnic, address, dateofbirth;
    RadioButton Malegender, FeMalegender;
    TextView Gender;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String mUserId;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebasedatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller);
        firstname = (EditText) findViewById(firstnamebtn);
        lastname = (EditText) findViewById(R.id.lastnamebtn);
        Email = (EditText) findViewById(R.id.emailbtn);
        password = (EditText) findViewById(R.id.passwordbtn);
        retype_password = (EditText) findViewById(R.id.retypepasswordbtn);
        Gender=(TextView)findViewById(R.id.genderText);
        Malegender = (RadioButton) findViewById(R.id.malebtn);
        FeMalegender = (RadioButton) findViewById(R.id.femalebtn);
        dateofbirth = (EditText) findViewById(R.id.dateofbirthinput);
        phone = (EditText) findViewById(R.id.phoneInput);
        cnic = (EditText) findViewById(R.id.cnicinput);
        address = (EditText) findViewById(R.id.address);
        btn = (Button) findViewById(R.id.signupbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
       // mFirebaseUser = mAuth.getCurrentUser();
        mFirebasedatabase = FirebaseDatabase.getInstance().getReference();
       // mUserId = mFirebaseUser.getUid();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = firstname.getText().toString();
                final String lastName = lastname.getText().toString();
                final String email = Email.getText().toString().trim();
                final String Password = password.getText().toString().trim();
                final String GenderValue=Gender.getText().toString();
                final String Retype_password = retype_password.getText().toString();
                final String Phone = phone.getText().toString();
                final String Cnic = cnic.getText().toString();
                final String Address = address.getText().toString();
                final String Dateofbirth = dateofbirth.getText().toString();
                if (firstName.length() == 0) {
                    firstname.requestFocus();
                    firstname.setError("Field Cannot Be Empty");
                } else if (!firstName.matches("[a-zA-Z]+")) {
                    firstname.requestFocus();
                    firstname.setError("Enter Only Alphabatical Charecters");
                } else if (lastName.length() == 0) {
                    lastname.requestFocus();
                    lastname.setError("Field Cannot Be Empty");
                } else if (!lastName.matches("[a-zA-Z]+")) {
                    lastname.requestFocus();
                    lastname.setError("Enter Only Alphabatical Charecters");
                } else if (email.length() == 0) {
                    Email.requestFocus();
                    Email.setError("Field Cannot Be Empty");
                } else if (Password.length() <= 8) {
                    password.requestFocus();
                    password.setError("Password must be at least 8 characters long");
                } else if (Retype_password.length() == 0) {
                    retype_password.requestFocus();
                    retype_password.setError("Field Cannot Be Empty");
                } else if(!(Password.equals(Retype_password))){
                    retype_password.requestFocus();
                    retype_password.setError("Password not match");
                }else if (!Malegender.isChecked() && !FeMalegender.isChecked()) {
                    Toast.makeText(Traveller.this,"Select gender",Toast.LENGTH_LONG).show();
                } else if (Phone == null) {
                    phone.requestFocus();
                    phone.setError("Field Cannot Be Empty");
                } else if (Cnic == null) {
                    cnic.requestFocus();
                    cnic.setError("Field Cannot Be Empty");
                } else if (Address.length() == 0) {
                    address.requestFocus();
                    address.setError("Field Cannot Be Empty");
                } else {
                    progressDialog.setMessage("Registration wait please");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(Traveller.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                mFirebaseUser = mAuth.getCurrentUser();
                                mUserId = mFirebaseUser.getUid();

                                writeNewUser(mUserId,firstName,lastName,email,Password,GenderValue,Dateofbirth,Address,Phone,Cnic);
                                Toast.makeText(Traveller.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(Traveller.this,ProfileImage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                //display some message here
                                Toast.makeText(Traveller.this,"Registration Error",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                    /*boolean result=travellerDBhelper.insertData(firstName,lastName,email,Password,Gender,Dateofbirth,Phone,Cnic,Address);
                    if(result==true){
                        Toast.makeText(Traveller.this," inserted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Traveller.this,ProfileImage.class));
                }else {
                    Toast.makeText(Traveller.this,"Not inserted", Toast.LENGTH_LONG).show();
                    }*/
                }

            }
        });
    }

        private void writeNewUser(String userId,String firstName, String lastName, String email, String Password,String Gender, String Dateofbirth,String Address, String Phone, String Cnic) {
            Firebase_Traveller_User user = new Firebase_Traveller_User(firstName,lastName,email,Password,Gender,Dateofbirth,Phone,Cnic,Address);
            mFirebasedatabase.child("Users").child(userId).setValue(user);
        }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.malebtn:
                if (checked)
                    // Pirates are the best
                Gender.setText("Male");
                    break;
            case R.id.femalebtn:
                if (checked)
                    Gender.setText("Female");
                    break;
        }
    }
    }
