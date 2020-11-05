package com.will.downsyndromeui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class NumberFlashcard extends AppCompatActivity {

    private TextToSpeech mTTS;

    SpannableString sentanceone[] = new SpannableString[7];
    String sentanceNumber[] = new String[7];
    SpannableString sentanceExplained[] = new SpannableString[7];
    SpannableString sentanceLast[] = new SpannableString[7];
    ArrayList<Integer> array_image = new ArrayList<>();
    ArrayList<String> image_name = new ArrayList<>();
    String numberInWords[], texts;
    int currentIndex = 0 ;

    TextView tvfirstSentce  , tvSecondSentance ,tvThirdSentace ;
    Button btnNext, btnBack;
    ImageView image;
    ImageButton speech;

    ForegroundColorSpan selectedText = new ForegroundColorSpan(Color.parseColor("#FE4331"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_flashcard);

        generateValues();

        tvfirstSentce = findViewById(R.id.firstSentance);
        tvSecondSentance = findViewById(R.id.SecondSentance);
        tvThirdSentace = findViewById(R.id.ThirdSentance);
        btnNext = findViewById(R.id.nextBtn);
        image = findViewById(R.id.imageView13);
        btnBack = findViewById(R.id.btnBack);
        speech = findViewById(R.id.imageButton3);

        tvfirstSentce.setText(sentanceone[0]);
        tvSecondSentance.setText(sentanceExplained[0]);
        tvThirdSentace.setText(sentanceLast[0]);
        image.setImageResource(array_image.get(0));

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){

                        Log.e("TTS", "Language not supported");
                    }else{

                        speech.setEnabled(true);

                    }
                }else{
                    Log.e("TTS", "Initialization Failed");
                }
            }
        });

        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(tvfirstSentce.getText().toString().substring(tvfirstSentce.length()-2, tvfirstSentce.length()-1));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentIndex == 6 ){
                    currentIndex = 0 ;
                }else{
                    currentIndex ++ ;
                }

                tvfirstSentce.setText(sentanceone[currentIndex]);
                tvSecondSentance.setText(sentanceExplained[currentIndex]);
                tvThirdSentace.setText(sentanceLast[currentIndex]);
                image.setImageResource(array_image.get(currentIndex));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NumberFlashcard.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    private void speak(String text){
        texts = text;
        float pitch= 1f;
        float  speed = 1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public String wordWithDash(String a){
        String Temp = "" ;
        for (int i = 0 ; i < a.length() ; i++){
            if(i == 0 ){
                Temp = a.charAt(i) + "" ;
            }else{
                Temp = Temp + "-" + a.charAt(i) ;
            }
        }

        return Temp ;
    }

    public void PopulateImages(){
        array_image.add(R.drawable.apple);
        array_image.add(R.drawable.frog);
        array_image.add(R.drawable.caterpillars);
        array_image.add(R.drawable.oranges);
        array_image.add(R.drawable.dinosaurs);
        array_image.add(R.drawable.avengers);
        array_image.add(R.drawable.strawberries);

        image_name.add("Apple");
        image_name.add("Frogs");
        image_name.add("Caterpillars");
        image_name.add("Oranges");
        image_name.add("Dinosaurs");
        image_name.add("Avengers");
        image_name.add("Strawberries");
    }
    public void generateValues(){
        for(int i = 0 ; i  <7 ; i++){
            sentanceNumber[i] = (i+1) + "" ;
        }

        PopulateImages();
        numberInWords = new String[]{
                "ONE",
                "TWO",
                "THREE",
                "FOUR",
                "FIVE",
                "SIX",
                "SEVEN"};

        String tempSentanceOne = "" ;
        for(int j =0 ; j < 7 ; j++){
            tempSentanceOne  = "This is the number " + sentanceNumber[j] + ".";

            sentanceone[j] = new SpannableString(tempSentanceOne);
            sentanceone[j].setSpan(selectedText , 19 , 19+sentanceNumber[j].length()  , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tempSentanceOne = "It is spelt "+ wordWithDash(numberInWords[j]);

            sentanceExplained[j] = new SpannableString(tempSentanceOne);
            sentanceExplained[j].setSpan(selectedText ,12 , 12 + wordWithDash(numberInWords[j]).length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

            tempSentanceOne = "Below is " + sentanceNumber[j] + " " + image_name.get(j) ;

            sentanceLast[j] = new SpannableString(tempSentanceOne);
            sentanceLast[j].setSpan(selectedText , 9 , 9 +sentanceNumber[j].length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        }
    }
}