package com.will.downsyndromeui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Sentences extends AppCompatActivity {

    Button btnBack, btnNext;
    TextView lblSentence;
    EditText txtSentence;
    String sentence, correctSentence, randomSentence = "";
    int pos=0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<String> split_sentence = new ArrayList<>();
    private ArrayList<String> the_sentence = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences);
        LoadSentences();

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        lblSentence = findViewById(R.id.lblSentence);
        txtSentence = findViewById(R.id.txtSentence);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sentences.this, Dashboard.class);
                startActivity(intent);
            }
        });

        RandomiseSentence();
        lblSentence.setText(randomSentence);
        correctSentence = the_sentence.get(pos);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence = txtSentence.getText().toString().trim();
                if(!sentence.isEmpty()){
                    if(sentence.equalsIgnoreCase(correctSentence)){
                        Toast.makeText(Sentences.this, "Good job! You got it right!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Sentences.this, "Oops, you made a little mistake. Keep going, you can do it!", Toast.LENGTH_SHORT).show();
                    }
                    pos++;
                }
                if(pos != the_sentence.size()-1){
                    RandomiseSentence();
                    lblSentence.setText(randomSentence);
                    correctSentence = the_sentence.get(pos);
                }else{
                    Toast.makeText(Sentences.this, "Congratulations! You worked at un-jumbling 7 different sentences!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Sentences.this, Dashboard.class);
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sentences.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    public void RandomiseSentence(){
        Random random = new Random();
        ArrayList<String> randomised = new ArrayList<>();
        split_sentence.clear();
        txtSentence.setText("");
        randomSentence = "";
        //split sentence in array and sends the split sentence to a new array
        String sen = the_sentence.get(pos).toString();
        Scanner split = new Scanner(sen).useDelimiter(" ");
        while(split.hasNext()){
            split_sentence.add(split.next());
        }

        for(int i=0; i<split_sentence.size(); i++){
            int index = random.nextInt(split_sentence.size());
            while(randomised.contains(split_sentence.get(index))){
                index = random.nextInt(split_sentence.size());
            }
            randomised.add(split_sentence.get(index));
        }

        for(int j=0; j<randomised.size(); j++){
            randomSentence += randomised.get(j)+" ";
        }

    }

    public void LoadSentences(){
        the_sentence.add("Sally kicks the ball");
        the_sentence.add("Julia ate an apple");
        the_sentence.add("Please may I have some juice");
        the_sentence.add("I enjoy playing with my toys");
        the_sentence.add("I have a pet");
        the_sentence.add("My pet is a dog");
        the_sentence.add("I love my family");
        the_sentence.add("My friends are special");
        the_sentence.add("Thank you for helping me");
    }

}