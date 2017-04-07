package com.example.umairali.easyjourney;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umairali.easyjourney.images.Owner_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class owner extends AppCompatActivity {
    Button btn;
    EditText firstname,lastname,Email,password,retype_password,phone,cnic,licence_number,registration_number,address;
    TextView dateofbirth;
    RadioButton Malegender,FeMalegender;
    TextView Gender;
    private Calendar calendar;
    private int year, month, day;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String mUserId;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebasedatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        firstname=(EditText)findViewById(R.id.firstnamebtn);
        lastname=(EditText)findViewById(R.id.lastnamebtn);
        Email=(EditText)findViewById(R.id.emailbtn);
        password=(EditText)findViewById(R.id.passwordbtn);
        retype_password=(EditText)findViewById(R.id.retypepasswordbtn);
        Gender=(TextView)findViewById(R.id.genderText);
        Malegender=(RadioButton)findViewById(R.id.malebtn);
        FeMalegender=(RadioButton)findViewById(R.id.femalebtn);
        dateofbirth=(TextView) findViewById(R.id.dateofbirthinput);
        showDateOfBirth(year, month+1, day);
        phone=(EditText)findViewById(R.id.phoneInput);
        cnic=(EditText)findViewById(R.id.cnicinput);
        licence_number=(EditText)findViewById(R.id.licenceinput);
        registration_number=(EditText)findViewById(R.id.vehicalRegistrationinput);
        address=(EditText)findViewById(R.id.address);
        btn=(Button)findViewById(R.id.signupbtn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebasedatabase = FirebaseDatabase.getInstance().getReference();
       // mUserId = mFirebaseUser.getUid();
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String firstName=firstname.getText().toString();
                final String lastName=lastname.getText().toString();
                final String email=Email.getText().toString().trim();
                final String Password=password.getText().toString();
                final String GenderValue=Gender.getText().toString();
                final String Retype_password=retype_password.getText().toString();
                final String Phone= phone.getText().toString();
                final String Cnic= cnic.getText().toString();
                final String Licence= licence_number.getText().toString();
                final String Registration= registration_number.getText().toString();
                final String Address=address.getText().toString();
                final String Dateofbirth=dateofbirth.getText().toString();
                if (firstName.length()==0){
                    firstname.requestFocus();
                    firstname.setError("Field Cannot Be Empty");
                }else if(!firstName.matches("[a-zA-Z]+")){
                    firstname.requestFocus();
                    firstname.setError("Enter Only Alphabatical Charecters");
                }else if (lastName.length()==0){
                    lastname.requestFocus();
                    lastname.setError("Field Cannot Be Empty");
                }else if(!lastName.matches("[a-zA-Z]+")) {
                    lastname.requestFocus();
                    lastname.setError("Enter Only Alphabatical Charecters");
                }else if(email.length()==0) {
                    Email.requestFocus();
                    Email.setError("Field Cannot Be Empty");
                }
                else if(Password.length()<=6){
                    password.requestFocus();
                    password.setError("Password must be at least 8 characters long ");
                } else if (Retype_password.length() == 0) {
                    retype_password.requestFocus();
                    retype_password.setError("Field Cannot Be Empty");
                } else if(!(Password.equals(Retype_password))) {
                    retype_password.requestFocus();
                    retype_password.setError("Password not match");
                }else if(!Malegender.isChecked()&&!FeMalegender.isChecked()){
                    Toast.makeText(owner.this,"Select gender",Toast.LENGTH_LONG).show();
                } else if(Phone==null){
                    phone.requestFocus();
                    phone.setError("Field Cannot Be Empty");
                }else if(Cnic==null){
                    cnic.requestFocus();
                    cnic.setError("Field Cannot Be Empty");
                }else if(Licence==null){
                    licence_number.requestFocus();
                    licence_number.setError("Field Cannot Be Empty");
                }else if(Registration==null){
                    registration_number.requestFocus();
                    registration_number.setError("Field Cannot Be Empty");
                }else if(Address.length()==0){
                    address.requestFocus();
                    address.setError("Field Cannot Be Empty");
                }else{
                    progressDialog.setMessage("Registration wait please");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(owner.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //display some message here
                                mFirebaseUser = mAuth.getCurrentUser();
                                mUserId = mFirebaseUser.getUid();

                                writeNewUser(mUserId,firstName,lastName,email,Password,GenderValue,Dateofbirth,Address,Phone,Cnic,Registration,Licence);
                                Toast.makeText(owner.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(owner.this,Owner_Profile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                //display some message here
                                Toast.makeText(owner.this,"Registration Error",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }

            }
        });

    }

    private void writeNewUser(String userId, String firstName, String lastName, String email, String password,String Gender,String dateofbirth,String address, String phone, String cnic, String registration, String licence) {
        Firebase_Owner_User user= new Firebase_Owner_User(firstName,lastName,email,password,Gender,dateofbirth,phone,cnic,licence,registration,address);
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
    @SuppressWarnings("deprecation")
    public void setDateB(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            DatePickerDialog dialog=new DatePickerDialog(this,myDateListener, year, month, day);
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDateOfBirth(arg1, arg2+1, arg3);
        }
    };

    private void showDateOfBirth(int year, int month, int day) {dateofbirth.setText(new StringBuilder().append(day).append("/")
            .append(month).append("/").append(year));
    }

}
