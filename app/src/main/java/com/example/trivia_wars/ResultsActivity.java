package com.example.trivia_wars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvRights, tvWrongs, tvTotal;
    private LinearLayout linearLayout;
    private Button btnAgain, btnBrowse, btnHighscore;
    private String selectedQuiz, selectedCategory, userID;
    private  int strRights, strWrongs, strTotal;

    //Get database reference to read or write from firebase database
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        tvRights = findViewById(R.id.tv_rights);
        tvWrongs = findViewById(R.id.tv_wrongs);
        tvTotal = findViewById(R.id.tv_total);
        btnAgain = findViewById(R.id.btn_again);
        btnBrowse = findViewById(R.id.btn_browse);
        btnHighscore = findViewById(R.id.btn_highscore);
        linearLayout = findViewById(R.id.resultlayout);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();

        strRights = getIntent().getIntExtra("Correct",0);
        strWrongs = getIntent().getIntExtra("Wrong",0);
        strTotal = getIntent().getIntExtra("Total",0);

        tvTotal.setText("Total Questions: " + strTotal);
        tvRights.setText("Correct: " + strRights);
        tvWrongs.setText("Wrong: " + strWrongs);

        selectedCategory = getIntent().getStringExtra("selectedCat");
        selectedQuiz = getIntent().getStringExtra("selectedQ");

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, GenreActivity.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, TakequizActivity.class);
                intent.putExtra("selectedCategory", selectedCategory);
                intent.putExtra("selectedQuiz", selectedQuiz);
                startActivity(intent);
                finish();
            }
        });

        btnHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, HighscoreActivity.class));
                finish();
            }
        });

        //TO SAVE HIGH SCORE
        Score score = new Score(selectedQuiz, selectedCategory, strRights);
        //We add values to the "users" path using setValue and indicate the identifier (key) using .child(name)
        mDatabase.child(userID).child(selectedQuiz).setValue(score)
                //action if successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(linearLayout, "New high score!", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                })
                //action if unsuccessful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                        Snackbar.make(linearLayout, "Score not recorded!", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
    }
}