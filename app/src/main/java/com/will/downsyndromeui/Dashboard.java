package com.will.downsyndromeui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.R.layout.simple_spinner_dropdown_item;

public class Dashboard extends AppCompatActivity {

    private User currentStudent = new User();
    String welcomeMessage = "Welcome ";
    TextView welcome, txtCurrentLevel;
    Spinner level;
    ImageView colorAndShape, animals, words, puzzle, numbers, pronouns, social, sentences;
    Button logout, settings;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    List<String> levels = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        level = findViewById(R.id.spnLevelList);
        logout = findViewById(R.id.btnLogout);
        settings = findViewById(R.id.btnSettings);
        welcome = findViewById(R.id.txtWelcome);
        txtCurrentLevel = findViewById(R.id.currentLevel);
        progressBar = findViewById(R.id.progressBar);
        colorAndShape = findViewById(R.id.imgColourAndShape);
        animals = findViewById(R.id.imgAnimal);
        words = findViewById(R.id.imgWords);
        sentences = findViewById(R.id.imgSentences);
        numbers = findViewById(R.id.imgNumbers);
        pronouns = findViewById(R.id.imgPronouns);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot child:children){
                    User pulledUser = child.getValue(User.class);
                    if(pulledUser.getEmail().equalsIgnoreCase(currentUser.getEmail())){
                        currentStudent = pulledUser;
                        welcomeMessage = welcomeMessage+pulledUser.getUsername();
                        welcome.setText(welcomeMessage);

                        PopulateLevelDetails();
                        arrayAdapter = new ArrayAdapter<String>(Dashboard.this, simple_spinner_dropdown_item, levels);
                        arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
                        level.setAdapter(arrayAdapter);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Settings.class);
                startActivity(intent);
            }
        });

        colorAndShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FlashCard.class);
                intent.putExtra("Origin", "Colours and Shapes");
                startActivity(intent);
            }
        });

        animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FlashCard.class);
                intent.putExtra("Origin", "Animals");
                startActivity(intent);
            }
        });

        words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FlashCard.class);
                intent.putExtra("Origin", "Words");
                startActivity(intent);
            }
        });

        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NumberFlashcard.class);
                startActivity(intent);
            }
        });

        sentences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Sentences.class);
                startActivity(intent);
            }
        });

        pronouns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FlashCard.class);
                intent.putExtra("Origin", "Pronouns");
                startActivity(intent);
            }
        });

    }

    public void PopulateLevelDetails(){
        int currentLevel = currentStudent.getLevel();
        for(int i=1; i<= currentLevel; i++){
            levels.add("Level "+i);
        }

        txtCurrentLevel.setText(currentLevel+"");
        progressBar.setProgress(currentStudent.getLevelXP());

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}
