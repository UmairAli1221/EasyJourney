package com.example.umairali.easyjourney;

/**
 * Created by GH on 1/9/2017.
 */

public class Firebase_Owner_User {
    public String firstname, lastname, email, password, gender, dateofbirth, address;
    public String phone, cnic, licence_number, registration_number;

    public Firebase_Owner_User() {

    }

    public Firebase_Owner_User(String firstname, String lastname, String email, String password, String gender, String dateofbirth, String address, String phone, String cnic, String licence_number, String registration_number) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.address = address;
        this.phone = phone;
        this.cnic = cnic;
        this.licence_number = licence_number;
        this.registration_number = registration_number;
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

    public String getLicence_number() {
        return licence_number;
    }

    public String getRegistration_number() {
        return registration_number;
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

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }
}