package com.example.eugene_kachanouski.softwaredesignproject;

import android.net.Uri;

public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photo = "";

    public Profile(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Profile() {
//        this.firstName = "";
//        this.lastName = "";
//        this.email = "";
//        this.phone = "";
//        this.photo = Uri.parse("");
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
