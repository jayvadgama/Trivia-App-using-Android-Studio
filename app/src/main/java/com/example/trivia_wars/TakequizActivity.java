package com.example.trivia_wars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TakequizActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView tvqTitle, tvQuestion;
    private List<Question> questList = new ArrayList<>();
    private String selectedQuiz, selectedCategory, selectedAnswer;
    private Button rb1, rb2, rb3, rb4;
    private int seconds, selectedChoice;
    private int total, wrong, correct = 0;
    private int currentQuestion = -1;
    private CountDownTimer countDownTimer;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takequiz);

        tvqTitle = findViewById(R.id.tv_qtitle);
        tvQuestion = findViewById(R.id.tv_question);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);

        selectedCategory = getIntent().getStringExtra("selectedCategory");
        selectedQuiz = getIntent().getStringExtra("selectedQuiz");

        getQuestionList();
        checkButtonClicked();


    }

    private void checkButtonClicked(){
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb1.getText().equals(questList.get(currentQuestion).getCorrectAnswer())){
                    rb1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    correct++;
                    total++;
                }else {
                    rb1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    if (rb2.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb3.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb4.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    }
                    wrong++;
                    total++;
                }
                resetColor();
                changeQuestion();
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb2.getText().equals(questList.get(currentQuestion).getCorrectAnswer())){
                    rb2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    correct++;
                    total++;
                }else {
                    rb2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    if (rb1.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb3.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb4.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    }
                    wrong++;
                    total++;
                }
                resetColor();
                changeQuestion();
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb3.getText().equals(questList.get(currentQuestion).getCorrectAnswer())){
                    rb3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    correct++;
                    total++;
                }else {
                    rb3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    if (rb2.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb1.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb4.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    }
                    wrong++;
                    total++;
                }
                resetColor();
                changeQuestion();
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb4.getText().equals(questList.get(currentQuestion).getCorrectAnswer())){
                    rb4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    correct++;
                    total++;
                }else {
                    rb4.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                    if (rb2.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb3.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    } else if (rb1.getText().equals(questList.get(currentQuestion).getCorrectAnswer())) {
                        rb1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
                    }
                    wrong++;
                    total++;
                }
                resetColor();
                changeQuestion();
            }
        });
    }

    private void resetColor() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Reset to original background color
                //rb1.setBackgroundColor(Color.parseColor("#191e39"));
                rb1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy)));
                //rb2.setBackgroundColor(Color.parseColor("#191e39"));
                rb2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy)));
                //rb3.setBackgroundColor(Color.parseColor("#191e39"));
                rb3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy)));
                //rb4.setBackgroundColor(Color.parseColor("#191e39"));
                rb4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy)));
            }
        },1500);
    }

    private void changeQuestion() {
        //Check if we are in the last question
        if(currentQuestion < questList.size()-1){
            //Not last question
            setQuestions();
        }else{
            //Last Question
            Intent intent = new Intent(TakequizActivity.this, ResultsActivity.class);
            intent.putExtra("Total",total);
            intent.putExtra("Correct", correct);
            intent.putExtra("Wrong", wrong);
            intent.putExtra("selectedCat", selectedCategory);
            intent.putExtra("selectedQ", selectedQuiz);

            startActivity(intent);
            //countDownTimer.cancel();
            finish();
        }
    }

    private void getQuestionList(){
        DocumentReference docRef = db.collection(selectedCategory).document(selectedQuiz);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        try {
                            Gson gson = new Gson();
                            String s1 = gson.toJson(document.getData());
                            JSONObject jsonObject = new JSONObject(s1);
                            JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());
                            JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject jsonContent = (JSONObject) jsonArray2.get(i);

                                String strQuestion = jsonContent.getString("question");
                                //System.out.println(strQuestion);
                                String strCAnswer = jsonContent.getString("correctAnswer");
                                //System.out.println(strCAnswer);
                                JSONArray strAnswers = jsonContent.getJSONArray("answerList");
                                //System.out.println(strAnswers);

                                List<String> temp = new ArrayList<>();

                                for (int k = 0; k<strAnswers.length(); k++){
                                    temp.add(strAnswers.get(k).toString());
                                }
                                initQuestions(new Question(strQuestion, temp, strCAnswer));
                            }
                            setQuestions();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void initQuestions(Question question){
        questList.add(question);
    }

    private void setQuestions(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestion++;
                int myno = currentQuestion + 1;
                tvqTitle.setText("Question " + myno);
                tvQuestion.setText(questList.get(currentQuestion).getQuestion());
                rb1.setText(questList.get(currentQuestion).getAnswerList().get(0));
                rb2.setText(questList.get(currentQuestion).getAnswerList().get(1));
                rb3.setText(questList.get(currentQuestion).getAnswerList().get(2));
                rb4.setText(questList.get(currentQuestion).getAnswerList().get(3));
            }
        }, 1500);
    }
}