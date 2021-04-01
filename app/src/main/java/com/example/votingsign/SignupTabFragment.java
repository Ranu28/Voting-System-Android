package com.example.votingsign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.votingsign.Common.Onboarding;
import com.example.votingsign.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class SignupTabFragment extends Fragment {

    float v = 0;
    TextView name, email, nic, mobile, password, confirm;
    Button signupbtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaneState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        nic = root.findViewById(R.id.nic);
        mobile = root.findViewById(R.id.mobile);

        password = root.findViewById(R.id.password);
        confirm = root.findViewById(R.id.confirm);

        signupbtn = root.findViewById(R.id.signupbtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirebase();
            }
        });

        name.setTranslationX(800);
        email.setTranslationX(800);
        nic.setTranslationX(800);
        mobile.setTranslationX(800);

        password.setTranslationX(800);
        confirm.setTranslationX(800);

        name.setAlpha(v);
        email.setAlpha(v);
        nic.setAlpha(v);
        mobile.setAlpha(v);

        password.setAlpha(v);
        confirm.setAlpha(v);

        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        nic.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();

        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        confirm.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        return root;
    }

    private boolean validateName() {
        String val = name.getText().toString();
        if (val.isEmpty()) {
            name.setError("Field should not be empty");
            return false;
        } else {
            name.setError(null);

            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field should not be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validateNIC() {
        String val = nic.getText().toString();
        String nicPattern = "[0-9]{12}";
        String nicPattern2 = "[0-9]{9}^\\d+[v]?$";

        if (val.isEmpty()) {
            nic.setError("Field should not be empty");
            return false;

        } else if (val.length() > 12) {
            nic.setError("NIC too long");
            return false;
        } else if (val.length() < 10) {
            nic.setError("NIC too short");
            return false;
        } else if (val.length() == 12) {
            if (!val.matches(nicPattern)) {
                nic.setError("If your NIC has 12 characters, all should be numeric");
                return false;
            } else {
                nic.setError(null);
                return true;
            }

        } else if (val.length() == 11) {


                nic.setError("Entered NIC is invalid");
                return false;

        }
        else if(val.length()==10){
            if(!(nic.getText().toString().trim().matches("^[0-9]{9}[vVxX]$"))){
                nic.setError("If your NIC has 10 characters,it should include 10 digits and letter v at the end");
                return false;
            }else{
                nic.setError(null);
                return true;
            }
        }

        else {
            nic.setError(null);
            return true;
        }
    }



    private boolean validateMobile(){

        String val = mobile.getText().toString();
        String MobilePattern = "[0-9]{10}";
        if (val.isEmpty()){
            mobile.setError("Field should not be empty");
            return false;
        }

        else if (!val.matches(MobilePattern)) {
            mobile.setError("Please enter valid 10 digit phone number");
            return false;
        }
        else {
            mobile.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getText().toString();

        // 8 character
        if (val.length() < 6) {
            password.setError("Password should have at least 6 characters");
            return false;

        }
        //number,capital letter,symbol
        else if (!val.matches("(.*[0-9].*)") | !val.matches("(.*[A-Z].*)") | !val.matches("^(?=.*[_.()$&@]).*$") ) {
            password.setError("Password is too weak.It should contain at least 1 number,1 upper case letter and a special symbol");
            return false;
        }


      else if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


    private boolean validateConfirm(){
        String val=confirm.getText().toString();

        if(val.isEmpty()){
            confirm.setError("Field cannot be empty");
            return false;
        }

        else{
            confirm.setError(null);
            return true;
        }
    }

    private boolean passmatch(){

        if(!password.getText().toString().equals(confirm.getText().toString())){

            confirm.setError("Passwords do not match.");
            return false;

        }
        else {
            confirm.setError(null);
            return true;
        }

    }


    private void saveDataToFirebase() {

        if(!validateName() |!validatePassword() | !validateMobile() | !validateNIC() | !validateEmail() | !validateConfirm() | !passmatch())
        {
            return;
        }

        String uname1 = name.getText().toString();
        String uemail = email.getText().toString();
        String unic = nic.getText().toString();
        String umobile = mobile.getText().toString();
        String upassword = password.getText().toString();

        Users u = new Users(uname1, uemail, unic, umobile, upassword);


            db.collection("Users").add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {


                    Toast.makeText(getContext(), "User Saved!!!", Toast.LENGTH_LONG).show();

                 name.setText(null);
                 email.setText(null);
                 nic.setText(null);
                 mobile.setText(null);
                 password.setText(null);
                 confirm.setText(null);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Save failed!!!", Toast.LENGTH_LONG).show();

                }
            });

        }

}
