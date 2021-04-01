package com.example.votingsign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.votingsign.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class LoginTabFragment extends Fragment {

    float v=0;
    TextView username,pass;
    Button loginbtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaneState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);


    username=root.findViewById(R.id.username);
    pass=root.findViewById(R.id.pass);

    loginbtn = root.findViewById(R.id.loginbtn);


    loginbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verifylogin();
        }
    });



    username.setTranslationX(800);
    pass.setTranslationX(800);

    username.setAlpha(v);
    pass.setAlpha(v);

    username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
    pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        return root;
    }



    private void verifylogin() {

        if(!validateEmail() | !validatePassword())
        {
            return;
        }

        String email = username.getText().toString();
        String password = pass.getText().toString();

        db.collection("Users").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();

                if(documentSnapshotList.size()>0) {
                    DocumentSnapshot d = documentSnapshotList.get(0);

                    Users users = d.toObject(Users.class);
                    String  mail = users.getEmail();
                    String pw = users.getPassword();
                    String uid = d.getId();
                    users.setUserid(uid);

                    if (mail.equals(email) && pw.equals(password)){

                        Intent loginIntent = new Intent(getContext(),UserProfile.class);

                        loginIntent.putExtra("email",users.getEmail());

                        loginIntent.putExtra("mobile",users.getMobile());
                        loginIntent.putExtra("name",users.getName());;
                        loginIntent.putExtra("nic",users.getNic());
                        loginIntent.putExtra("pw",users.getPassword());
                        loginIntent.putExtra("uid",uid);

                        startActivity(loginIntent);

                    }else {

                        Toast.makeText(getContext(), "Cannot Find Account.Try Signup.", Toast.LENGTH_SHORT).show();
                        Intent registerIntent = new Intent(getContext(),SignupTabFragment.class);
                        startActivity(registerIntent);

                    }

                }else{
                    Toast.makeText(getContext(), "Something went wrong.Try Again later.", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Check Your Connection.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void loginUser() {
        //Validate Login Info
        if (!validateEmail() | !validatePassword()) {
            return;
        }
    }

    private boolean validateEmail(){
        String val = username.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            username.setError("Field should not be empty");
            return false;
        }
        else if (!val.matches(emailPattern)) {
            username.setError("Invalid email address");
            return false;
        }
        else {
            username.setError(null);
            return true;
        }
    }


    private Boolean validatePassword() {
        String val = pass.getText().toString();
        if (val.isEmpty()) {
            pass.setError("Field cannot be empty");
            return false;
        }
        else if (val.length() < 6 | !val.matches("(.*[0-9].*)") | !val.matches("(.*[A-Z].*)") | !val.matches("^(?=.*[_.()$&@]).*$") ) {
            pass.setError("Invalid Password");
            return false;
        }


        else {
            pass.setError(null);
            return true;
        }
    }



}
