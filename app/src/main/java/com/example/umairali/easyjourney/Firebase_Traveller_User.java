package com.example.umairali.easyjourney;

/**
 * Created by GH on 1/9/2017.
 */

public class Firebase_Traveller_User {
    public String firstname, lastname, email, password,gender,dateofbirth,address;
    public String phone, cnic;

    public Firebase_Traveller_User() {

    }

    public Firebase_Traveller_User(String firstname, String lastname, String email, String password, String gender, String dateofbirth, String address, String phone, String cnic) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.address = address;
        this.phone = phone;
        this.cnic = cnic;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFirstname() {

        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getCnic() {
        return cnic;
    }
}