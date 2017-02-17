package com.example.umairali.easyjourney.SqliteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GH on 12/18/2016.
 */

public class OwnerDBhelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "OwnerEasyJourney.db";
    public static final String TABLE_NAME = "owner_table";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "FirstName";
    public static final String Col_3 = "LastName";
    public static final String Col_4 = "Email";
    public static final String Col_5 = "Password";
    public static final String Col_6 = "Gender";
    public static final String Col_7 = "DateOfBirth";
    public static final String Col_8 = "PhoneNumber";
    public static final String Col_9 = "CNICNumber";
    public static final String Col_10 = "LicenceNumber";
    public static final String Col_11 = "RegistrationNumber";
    public static final String Col_12 = "Address";
    public static final String Col_13 = "Profile";
    public static final String Col_14 = "CNIC";
    public static final String Col_15 = "Licence";
    public static final String Col_16 = "NumberPlateNumber";


    public OwnerDBhelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,Email TEXT," +
                "Password VARCHAR,Gender VARCHAR, DateOfBirth VARCHAR,PhoneNumber TINYINT, CNICNumber TINYINT," +
                " LicenceNumber TINYINT,RegistrationNumber  TINYINT,Address VARCHAR,Profile BLOB,CNIC BLOB,Licence BLOB,NumberPlateNumber BLOB)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS" + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String first_name, String last_name, String email, String password, String gender, String dateofbirth,
                              int phone_number, int cnic_number, int licence_number, int registration, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName", first_name);
        contentValues.put("LastName", last_name);
        contentValues.put("Email", email);
        contentValues.put("Password", password);
        contentValues.put("Gender", gender);
        contentValues.put("DateofBirth", dateofbirth);
        contentValues.put("PhoneNumber", phone_number);
        contentValues.put("CNICNumber", cnic_number);
        contentValues.put("LicenceNumber", licence_number);
        contentValues.put("RegistrationNumber", registration);
        contentValues.put("Address", address);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {

            return false;
        } else {
            return true;
        }

    }
}