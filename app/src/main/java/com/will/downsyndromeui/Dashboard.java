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
    String welcomeMessage = "Welcome ", username;
    int level, XP;
    TextView welcome, txtCurrentLevel;
    Spinner spnLevel;
    ImageView colorAndShape, animals, words, puzzle, numbers, pronouns, social, sentences;
    Button logout, settings, gallery;
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
        spnLevel = findViewById(R.id.spnLevelList);
        logout = findViewById(R.id.btnLogout);
        settings = findViewById(R.id.btnSettings);
        gallery = findViewById(R.id.btnGallery);
        welcome = findViewById(R.id.txtWelcome);
        txtCurrentLevel = findViewById(R.id.currentLevel);
        progressBar = findViewById(R.id.progressBar);
        colorAndShape = findViewById(R.id.imgColourAndShape);
        animals = findViewById(R.id.imgAnimal);
        words = findViewById(R.id.imgWords);
        sentences = findViewById(R.id.imgSentences);
        numbers = findViewById(R.id.imgNumbers);
        pronouns = findViewById(R.id.imgPronouns);

        SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
        username = userDetails.getString("username", "");
        level = userDetails.getInt("level",1);
        XP = userDetails.getInt("XP", 0);

        welcomeMessage = welcomeMessage+username;
        welcome.setText(welcomeMessage);

        txtCurrentLevel.setText(level+"");
        progressBar.setProgress(currentStudent.getLevelXP());
        PopulateLevelList();

        arrayAdapter = new ArrayAdapter<String>(Dashboard.this, simple_spinner_dropdown_item, levels);
        arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spnLevel.setAdapter(arrayAdapter);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Gallery.class);
                startActivity(intent);
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

                SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = userDetails.edit();
                editor.putString("username", "");
                editor.putInt("level", 1);
                editor.putInt("XP", 0);
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

    public void PopulateLevelList(){
        for(int i=1; i<= level; i++){
            levels.add("Level "+i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot child:children){
                    User pulledUser = child.getValue(User.class);
                    if(pulledUser.getEmail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())){
                        SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userDetails.edit();
                        editor.putString("username", pulledUser.getUsername());
                        editor.putInt("level", pulledUser.getLevel());
                        editor.putInt("XP", pulledUser.getLevelXP());
                        editor.apply();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed(){

    }
}
