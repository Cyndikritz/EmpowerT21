package com.will.downsyndromeui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    String name;
    String welcomeMessage = "Welcome ";
    TextView welcome;
    Spinner level;
    ImageView colorAndShape, animals, words, puzzle, numbers, pronouns, social, sentences;
    Button logout, settings;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String[] levels = {"Level 1", "Level 2", "Level 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        level = findViewById(R.id.spnLevel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(arrayAdapter);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                welcomeMessage = "Keep Practicing!";
            } else {
                name = extras.getString("child name");
                welcomeMessage = welcomeMessage+name;
            }
        } else {
            name = (String) savedInstanceState.getSerializable("child name");
        }

        logout = findViewById(R.id.btnLogout);
        settings = findViewById(R.id.btnSettings);
        welcome = findViewById(R.id.txtWelcome);
        welcome.setText(welcomeMessage);

        colorAndShape = findViewById(R.id.imgColourAndShape);
        animals = findViewById(R.id.imgAnimal);
        words = findViewById(R.id.imgWords);
        sentences = findViewById(R.id.imgSentences);
        numbers = findViewById(R.id.imgNumbers);
        pronouns = findViewById(R.id.imgPronouns);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
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

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }
}
