package com.will.downsyndromeui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class FlashCard extends AppCompatActivity {

    private TextToSpeech mTTS;
    String origin, texts;
    Button back, next;
    ImageView help, speech, image;
    TextView imageText;
    int pos = 0;

    ArrayList<Integer> array_image;
    ArrayList<String> image_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        back = findViewById(R.id.btnBack);
        next = findViewById(R.id.btnNext);
        help = findViewById(R.id.imgHelp);
        speech = findViewById(R.id.imgSound);
        image = findViewById(R.id.imgFlash);
        imageText = findViewById(R.id.txtWord);

        Bundle extras = getIntent().getExtras();
        origin  = extras.getString("Origin");

        image.setForeground(null);

        array_image = new ArrayList<Integer>();
        image_name = new ArrayList<String>();
        PopulateImageList();
        image.setImageResource(array_image.get(pos));
        imageText.setText(image_name.get(pos));

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
                speak(imageText.getText().toString());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                if(pos < array_image.size())
                {
                    image.setImageResource(array_image.get(pos));
                    imageText.setText(image_name.get(pos));
                }
                else
                {
                    Toast.makeText(FlashCard.this, "Congratulations, you practiced another "+pos+" words today!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(FlashCard.this, Dashboard.class);
                    startActivity(i);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashCard.this, Dashboard.class);
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

    public void PopulateImageList()
    {
        array_image.clear();
        switch(origin)
        {
            case "Colours and Shapes":
                array_image.add(R.drawable.red);
                image_name.add("Red");
                array_image.add(R.drawable.blue);
                image_name.add("Blue");
                array_image.add(R.drawable.square);
                image_name.add("Square");
                array_image.add(R.drawable.orange);
                image_name.add("Orange");
                array_image.add(R.drawable.circle);
                image_name.add("Circle");
                array_image.add(R.drawable.green);
                image_name.add("Green");
                array_image.add(R.drawable.yellow);
                image_name.add("Yellow");
                break;
            case "Words":
                array_image.add(R.drawable.apple);
                image_name.add("Apple");
                array_image.add(R.drawable.house);
                image_name.add("House");
                array_image.add(R.drawable.pencil);
                image_name.add("Pencil");
                array_image.add(R.drawable.fork);
                image_name.add("Fork");
                array_image.add(R.drawable.present);
                image_name.add("Present");
                break;
            case "Animals":
                array_image.add(R.drawable.lion);
                image_name.add("Lion");
                array_image.add(R.drawable.giraffe);
                image_name.add("Giraffe");
                array_image.add(R.drawable.cow);
                image_name.add("Cow");
                array_image.add(R.drawable.dog);
                image_name.add("Dog");
                array_image.add(R.drawable.pig);
                image_name.add("Pig");
                break;
            case "Pronouns":
                array_image.add(R.drawable.he);
                image_name.add("HE");
                array_image.add(R.drawable.they);
                image_name.add("I want to play with THEM");
                array_image.add(R.drawable.she);
                image_name.add("SHE");
                array_image.add(R.drawable.theytwo);
                image_name.add("THEY are happy");
                break;
        }

    }
}
