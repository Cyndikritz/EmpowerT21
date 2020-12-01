package com.will.downsyndromeui;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Settings extends AppCompatActivity implements LanguageDialog.LabguageDialogListener,usernameDialog.usernameListner, emailDialog.emailListner {


    Button chnageLanguagePreference ;
    public static final String CODE = "en";
    public static final String SHARED_CODE = "sharedcode";
    private String codeval ;

    Button ChangeUser ;
    Button ChangePassword ;
    Button ChangeEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);
        chnageLanguagePreference = (Button) findViewById(R.id.btnChangeLanguage);
        ChangeEmail = (Button) findViewById((R.id.btnChangeEmail));
        ChangeUser  = (Button) findViewById((R.id.btnChangeUsername));
        ChangePassword = (Button) findViewById(R.id.btnChangePassword) ;

        ChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailDialog();
            }
        });


        ChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUsernameDialog();
            }
        });

        chnageLanguagePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


    }



    private void openEmailDialog(){
        emailDialog aEmail = new emailDialog();
        aEmail.show(getSupportFragmentManager() , "Email Dialog");



    }
    private void openUsernameDialog(){

        usernameDialog aUsername = new usernameDialog() ;
        aUsername.show(getSupportFragmentManager(),"username Dialog");


    }
    private void openPasswordDialog(){}

    private void openDialog() {

        LanguageDialog lanDia = new LanguageDialog();
        lanDia.show(getSupportFragmentManager(),"Language Dialog"); 

    }


    private void setAppLocale(String LoCoded){
/*

        Locale mylocale = Locale.;
        Resources res =  getResources();
        DisplayMetrics dm =  res.getDisplayMetrics();
        Configuration conf =  res.getConfiguration();
        conf.locale = mylocale ;
        res.updateConfiguration(conf , dm );
        Locale.setDefault(conf.locale);


*/

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();

        config.setLocale(new Locale(LoCoded.toLowerCase()));
        res.updateConfiguration(config,dm);






    }


    public void savedata(String text){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_CODE , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CODE , text );


        editor.apply();



    }

    public void LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_CODE , MODE_PRIVATE);
        codeval = sharedPreferences.getString(CODE , "en");

        Toast.makeText(Settings.this, codeval + "   okay ", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void applySettings(String aCode) {
        Toast.makeText(Settings.this, aCode, Toast.LENGTH_SHORT).show();

        setAppLocale(aCode);
        savedata(aCode);

        Intent intent = Settings.this.getIntent();
        Settings.this.finish();
        startActivity(intent);



    }

    @Override
    public void ApplyUsername(String uName) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("username").setValue(uName);

        SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString("username", uName);
        editor.apply();


        Toast.makeText(Settings.this , "Success" , Toast.LENGTH_SHORT).show();



    }

    @Override
    public void ApplyEmail(String email) {

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(Settings.this, "Please provide a valid email", Toast.LENGTH_SHORT).show();


        }else{

            FirebaseAuth mAuth = FirebaseAuth.getInstance();


            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(email);



        }



    }
}