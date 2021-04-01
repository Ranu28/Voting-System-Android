package com.example.votingsign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingsign.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

EditText emailtxtx,mobiletxt,nictxt,passwordtxt;
EditText  nametxt;
Button updatebtn,deletebtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

String userdocid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);



        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String username = bundle.getString("un");
        String name = bundle.getString("name");
        String mobile = bundle.getString("mobile");
        String nic = bundle.getString("nic");
        String password = bundle.getString("pw");
        userdocid = bundle.getString("uid");

        nametxt = findViewById(R.id.nametxt);
        emailtxtx = findViewById(R.id.emailtxt);
        mobiletxt = findViewById(R.id.mobiletxt);
        nictxt = findViewById(R.id.nictxt);
        passwordtxt = findViewById(R.id.passwordtxt);

        emailtxtx.setText(email);
        nametxt.setText(name);
        mobiletxt.setText(mobile);
        nictxt.setText(nic);
        passwordtxt.setText(password);

        updatebtn = findViewById(R.id.updatebtn);
        deletebtn = findViewById(R.id.deletebtn);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteuserfromfirebase();
            }
        });

    }


    private boolean validateName() {
        String val = nametxt.getText().toString();
        if (val.isEmpty()) {
            nametxt.setError("Field should not be empty");
            return false;
        } else {
            nametxt.setError(null);

            return true;
        }
    }

    private boolean validateEmail() {
        String val = emailtxtx.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            emailtxtx.setError("Field should not be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailtxtx.setError("Invalid email address");
            return false;
        } else {
            emailtxtx.setError(null);
            return true;
        }
    }

    private boolean validateNIC() {
        String val = nictxt.getText().toString();
        String nicPattern = "[0-9]{12}";
        String nicPattern2 = "[0-9]{9}^\\d+[v]?$";

        if (val.isEmpty()) {
            nictxt.setError("Field should not be empty");
            return false;

        } else if (val.length() > 12) {
            nictxt.setError("NIC too long");
            return false;
        } else if (val.length() < 10) {
            nictxt.setError("NIC too short");
            return false;
        } else if (val.length() == 12) {
            if (!val.matches(nicPattern)) {
                nictxt.setError("If your NIC has 12 characters, all should be numeric");
                return false;
            } else {
                nictxt.setError(null);
                return true;
            }

        } else if (val.length() == 11) {


            nictxt.setError("Entered NIC is invalid");
            return false;

        }
        else if(val.length()==10){
            if(!(nictxt.getText().toString().trim().matches("^[0-9]{9}[vVxX]$"))){
                nictxt.setError("If your NIC has 10 characters,it should include 10 digits and letter v at the end");
                return false;
            }else{
                nictxt.setError(null);
                return true;
            }
        }

        else {
            nictxt.setError(null);
            return true;
        }
    }



    private boolean validateMobile(){

        String val = mobiletxt.getText().toString();
        String MobilePattern = "[0-9]{10}";
        if (val.isEmpty()){
            mobiletxt.setError("Field should not be empty");
            return false;
        }

        else if (!val.matches(MobilePattern)) {
            mobiletxt.setError("Please enter valid 10 digit phone number");
            return false;
        }
        else {
            mobiletxt.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = passwordtxt.getText().toString();

        // 8 character
        if (val.length() < 6) {
            passwordtxt.setError("Password should have at least 6 characters");
            return false;

        }
        //number,capital letter,symbol
        else if (!val.matches("(.*[0-9].*)") | !val.matches("(.*[A-Z].*)") | !val.matches("^(?=.*[_.()$&@]).*$") ) {
            passwordtxt.setError("Password is too weak.It should contain at least 1 number,1 upper case letter and a special symbol");
            return false;
        }


        else if (val.isEmpty()) {
            passwordtxt.setError("Field cannot be empty");
            return false;
        } else {
            passwordtxt.setError(null);
            return true;
        }
    }



    private void UpdateData(){


        if(!validateName() |!validatePassword() | !validateMobile() | !validateNIC() | !validateEmail())
        {
            return;
        }

        String upname=nametxt.getText().toString().trim();
        String upemail=emailtxtx.getText().toString().trim();
        String upmobile=mobiletxt.getText().toString().trim();
        String upnic=nictxt.getText().toString().trim();
        String uppassword=passwordtxt.getText().toString().trim();

        Users us=new Users(upname, upemail, upnic, upmobile, uppassword);

    db.collection("Users").document(userdocid).set(us).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Toast.makeText(UserProfile.this,"Updated successfully",Toast.LENGTH_LONG).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(UserProfile.this,"Error occured",Toast.LENGTH_LONG).show();
        }
    });
    }

    private void deleteuserfromfirebase() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Are You Sure You Want to Delete This User?");
        builder.setMessage("Action Cannot be Undo.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.collection("Users").document(userdocid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserProfile.this, "User Deleted.", Toast.LENGTH_SHORT).show();

                        nametxt.setText(null);
                        emailtxtx.setText(null);
                        nictxt.setText(null);
                        mobiletxt.setText(null);
                        passwordtxt.setText(null);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "Couldn't Find The User.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(UserProfile.this, "User not Deleted.", Toast.LENGTH_SHORT).show();

            }
        });

        AlertDialog ad = builder.create();
        ad.show();

    }


}







