package com.example.trivia_wars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnSignupPage, btnForgotPass;
    private EditText etEmail, etPassword;
    private String strEmail, strPassword;
    private ConstraintLayout constraintLayout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        btnSignupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotpassActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = etEmail.getText().toString();
                strPassword = etPassword.getText().toString();

                if (strEmail.isEmpty()|| !strEmail.contains("@")){
                    Snackbar.make(constraintLayout, "Please enter a valid email address!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }
                if (strPassword.isEmpty() || strPassword.length()<10){
                    Snackbar.make(constraintLayout, "Password must be 10 characters long!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }

                //authenticate user
                auth.signInWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Snackbar.make(constraintLayout, " " + task.getException(), BaseTransientBottomBar.LENGTH_LONG).show();
                                } else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (!user.isEmailVerified()) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, " " + task.getException(),
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                        Toast.makeText(LoginActivity.this, "We have sent you an email. Please verify your account before attempting to login.", Toast.LENGTH_SHORT).show();
                                        //Snackbar.make(constraintLayout, "We have sent you an email. Please verify your account before attempting to login.", BaseTransientBottomBar.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "You are now logged in", Toast.LENGTH_SHORT).show();
                                    }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });

    }

    private void initViews(){
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPass = findViewById(R.id.btn_forgotpass);
        btnSignupPage = findViewById(R.id.btn_signuppage);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        constraintLayout = findViewById(R.id.loginlayout);
    }
}