package com.example.trivia_wars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ImageButton btnTakeQuiz, btnCreateQuiz;
    private List<Question> questList = new ArrayList<>();
    private TextView tvWelcome;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakeQuiz = findViewById(R.id.btn_takequiz);
        btnCreateQuiz = findViewById(R.id.btn_createquiz);
        tvWelcome = findViewById(R.id.tv_showname);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser().reload();

        tvWelcome.setText("Welcome, " + auth.getCurrentUser().getEmail());

        tvWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighscoreActivity.class));
            }
        });

        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreatequizActivity.class));
            }
        });

        btnTakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GenreActivity.class));
            }
        });

    }
}