package com.example.votingsign.Model;

import android.widget.EditText;

import com.google.firebase.firestore.Exclude;

public class Users {

    @Exclude String userid;

    String name,email,nic,mobile,password;

    public Users() {
    }

    public Users(String name, String email, String nic, String mobile, String password) {
        this.name = name;
        this.email = email;
        this.nic = nic;
        this.mobile = mobile;

        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
