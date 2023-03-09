package com.example.trivia_wars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuizlistActivity extends AppCompatActivity implements RecyclerAdapter.ListItemClickListener {

    private LinearLayout linearLayout;
    private RecyclerView rvQuizzes;
    private TextView tvTitle;
    private List<String> myCategoryList = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private String selectedCategory;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizlist);

        linearLayout = findViewById(R.id.quizlistLayout);
        rvQuizzes = findViewById(R.id.rv_quizzes);
        tvTitle = findViewById(R.id.tv_cattittle);

        selectedCategory = getIntent().getStringExtra("selectedCategory");
        tvTitle.setText(selectedCategory);

        db.collection(selectedCategory)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                myCategoryList.add(document.getId());
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(QuizlistActivity.this);//Parameter: context, number of columns
                            rvQuizzes.setLayoutManager(layoutManager);
                            recyclerAdapter = new RecyclerAdapter(myCategoryList, QuizlistActivity.this);
                            rvQuizzes.setAdapter(recyclerAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void onListItemClick(int position) {
        String selectedQuiz = myCategoryList.get(position);
        Intent intent = new Intent(QuizlistActivity.this, TakequizActivity.class);
        intent.putExtra("selectedQuiz", selectedQuiz);
        intent.putExtra("selectedCategory", selectedCategory);
        startActivity(intent);
    }
}