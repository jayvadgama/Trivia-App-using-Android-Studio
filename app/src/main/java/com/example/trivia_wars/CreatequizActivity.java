package com.example.trivia_wars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CreatequizActivity extends AppCompatActivity {

    private Button btnCreateQuiz, btnAddQuestion;
    private EditText etQuestion, etAnswer1, etAnswer2, etAnswer3, etAnswer4, etCorrect, etTitle;
    private Spinner spnCategory;
    private LinearLayout linearLayout;
    private String strQuestion, strAns1, strAns2, strAns3, strAns4, strCorrect, category, strTitle;
    private List<Question> questList = new ArrayList<>();
    private List<String> myCategoryList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createquiz);

        initViews();

        myCategoryList.add("Select Category");

        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                myCategoryList.add(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myCategoryList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(dataAdapter);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    category = "";
                } else {
                    category = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "";
            }
        });


        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strQuestion = etQuestion.getText().toString();
                strTitle = etTitle.getText().toString();
                strAns1 = etAnswer1.getText().toString();
                strAns2 = etAnswer2.getText().toString();
                strAns3 = etAnswer3.getText().toString();
                strAns4 = etAnswer4.getText().toString();
                strCorrect = etCorrect.getText().toString();

                if (category.equals("")){
                    Snackbar.make(linearLayout, "Please choose a category!", BaseTransientBottomBar.LENGTH_LONG).show();
                }else if (strTitle.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a title!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strQuestion.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a question!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strAns1.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a first answer!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strAns2.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a second answer!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strAns3.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a third answer!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strAns4.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter a fourth answer!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (strCorrect.isEmpty()){
                    Snackbar.make(linearLayout, "Please enter the correct answer!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (!strCorrect.equals(strAns1) && !strCorrect.equals(strAns2) && !strCorrect.equals(strAns3) && !strCorrect.equals(strAns4)){
                    Snackbar.make(linearLayout, "The correct answer must be one of the choices!", BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    questList.add(new Question(strQuestion,
                            new ArrayList<String>() {{add(strAns1);add(strAns2);add(strAns3);add(strAns4);}}, strCorrect));
                    Snackbar.make(linearLayout, "Question added successfully", BaseTransientBottomBar.LENGTH_LONG).show();
                    etQuestion.getText().clear();
                    etAnswer1.getText().clear();
                    etAnswer2.getText().clear();
                    etAnswer3.getText().clear();
                    etAnswer4.getText().clear();
                    etCorrect.getText().clear();
                }
            }
        });


        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questList.isEmpty()){
                    Snackbar.make(linearLayout, "Please create a new question first!", BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }

                Quiz newQuiz = new Quiz(category, strTitle, questList);

                db.collection(newQuiz.getcategory()).document(newQuiz.gettitle())
                        .set(newQuiz)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                Toast.makeText(CreatequizActivity.this, "Quiz successfully created!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                startActivity(new Intent(CreatequizActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initViews(){
        btnCreateQuiz = findViewById(R.id.btn_create);
        btnAddQuestion = findViewById(R.id.btn_addquestion);
        etQuestion = findViewById(R.id.et_question);
        etTitle = findViewById(R.id.et_title);
        etAnswer1 = findViewById(R.id.et_answer1);
        etAnswer2 = findViewById(R.id.et_answer2);
        etAnswer3 = findViewById(R.id.et_answer3);
        etAnswer4 = findViewById(R.id.et_answer4);
        etCorrect = findViewById(R.id.et_correct);
        spnCategory = findViewById(R.id.spinner_category);
        linearLayout = findViewById(R.id.createlayout);
    }
}