package com.example.trivia_wars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GenreActivity extends AppCompatActivity implements RecyclerAdapter.ListItemClickListener {

    private List<String> myCategoryList = new ArrayList<>();
    private List<String> dlList = new ArrayList<>();
    private List<Question> questList = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();
    private LinearLayout linearLayout;
    private RecyclerAdapter recyclerAdapter;

    private RecyclerView rvCategories;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        linearLayout = findViewById(R.id.genreLayout);
        rvCategories = findViewById(R.id.rv_categories);

        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                myCategoryList.add(document.getId());
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(GenreActivity.this);//Parameter: context, number of columns
                            rvCategories.setLayoutManager(layoutManager);
                            recyclerAdapter = new RecyclerAdapter(myCategoryList, GenreActivity.this);
                            rvCategories.setAdapter(recyclerAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void onListItemClick(int position) {
        String selectedCat = myCategoryList.get(position);
        Intent artIntent = new Intent(GenreActivity.this, QuizlistActivity.class);
        artIntent.putExtra("selectedCategory", selectedCat);
        startActivity(artIntent);
    }
}