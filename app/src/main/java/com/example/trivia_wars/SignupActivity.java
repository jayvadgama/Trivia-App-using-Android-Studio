package com.example.trivia_wars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    private EditText etFullname, etEmail, etPassword, etCPassword;
    private Button btnRegister, btnOpenLogin;
    private String strFullname, strEmail, strPassword, strCPassword;
    private ConstraintLayout constraintLayout;
    private FirebaseAuth auth;
    private static final String TAG = "GoogleActivity";
    private int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Check if the user is already logged in and progress to main activity
        /*if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }*/

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        //Initialize Views
        initViews();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFullname = etFullname.getText().toString();
                strEmail = etEmail.getText().toString();
                strPassword = etPassword.getText().toString();
                strCPassword = etCPassword.getText().toString();

                if (strFullname.isEmpty()){
                    Snackbar.make(constraintLayout, "Please enter your name!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }
                if (strEmail.isEmpty()|| !strEmail.contains("@")){
                    Snackbar.make(constraintLayout, "Please enter a valid email address!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }
                if (strPassword.isEmpty() || strPassword.length()<10){
                    Snackbar.make(constraintLayout, "Password must be 10 characters long!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }
                /*if (strCPassword != strPassword){
                    Snackbar.make(constraintLayout, "Passwords do not match!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }*/
                //CREATE THE USER
                auth.createUserWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Snackbar.make(constraintLayout, " " + task.getException(), BaseTransientBottomBar.LENGTH_LONG).show();
                                } else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(strFullname).build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    if (!user.isEmailVerified()) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            Snackbar.make(constraintLayout, " " + task.getException(), BaseTransientBottomBar.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                        Toast.makeText(SignupActivity.this, "We have sent you an email. Please verify your account.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Registration was successful", Toast.LENGTH_SHORT).show();
                                    }
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });


            }
        });

        btnOpenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initViews(){
        btnRegister = findViewById(R.id.btn_signup);
        btnOpenLogin = findViewById(R.id.btn_loginpage);
        etFullname = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etCPassword = findViewById(R.id.et_cpassword);
        constraintLayout = findViewById(R.id.signuplayout);
    }
}