package com.example.trivia_wars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HighscoreActivity extends AppCompatActivity {

    private ListView listView;
    private List<Score> scoreList = new ArrayList<>();
    private List<String> temp = new ArrayList<>();
    private LinearLayout linearLayout;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String userID;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        listView = findViewById(R.id.lv_scores);

        //Get Firebase auth instance
        userID = auth.getCurrentUser().getUid();

        mDatabase.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    try {
                        Gson gson = new Gson();
                        String s1 = gson.toJson(task.getResult().getValue());
                        //Log.e("string", s1);

                        //Create a Json Object from the string
                        JSONObject jsonObject = new JSONObject(s1);
                        //Log.e("jsonObject", jsonObject.toString());
                        //Log.e("string", s1);

                        //Convert the jsonObject to a jsonArray
                        JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());

                        //loop through the array getting details and adding them to an arraylist
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonContent = (JSONObject) jsonArray.get(i);
                            int dbScore = Integer.parseInt(jsonContent.getString("score"));
                            Log.e("string", String.valueOf(dbScore));
                            String dbTitle = jsonContent.getString("title");
                            Log.e("string", dbTitle);
                            String dbCategory = jsonContent.getString("category");
                            Log.e("string", dbCategory);

                            Score score = new Score(dbTitle, dbCategory, dbScore);
                            scoreList.add(score);

                            String tempString = dbCategory + ": " + dbTitle + " - " + dbScore;
                            temp.add(tempString);

                            //arrayAdapter = new ArrayAdapter<Score>(HighscoreActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, scoreList);
                            //listView.setAdapter(arrayAdapter);

                            arrayAdapter = new ArrayAdapter<String>(HighscoreActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, temp);
                            listView.setAdapter(arrayAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

}