package com.will.downsyndromeui;


import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Settings extends AppCompatActivity implements LanguageDialog.LabguageDialogListener,usernameDialog.usernameListner, emailDialog.emailListner , passwordDialog.passwordInterface {


    Button chnageLanguagePreference ;
    public static final String CODE = "en";
    public static final String SHARED_CODE = "sharedcode";
    public static final String SCALE = "fontScale";
    private String codeval ;

    Button ChangeUser ;
    Button ChangePassword  , btnComplaint;
    Button ChangeEmail,  increaseAmt , decreaseAmt;
    TextView edtFontDisp ;

    EditText  mutliLine ;






    private String complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);
        chnageLanguagePreference = (Button) findViewById(R.id.btnChangeLanguage);
        ChangeEmail = (Button) findViewById((R.id.btnChangeEmail));
        ChangeUser  = (Button) findViewById((R.id.btnChangeUsername));
        ChangePassword = (Button) findViewById(R.id.btnChangePassword) ;
        increaseAmt = (Button) findViewById(R.id.btnInc);
        decreaseAmt = (Button) findViewById(R.id.btnDec);
        edtFontDisp = (TextView) findViewById(R.id.tvTxtSize);
        mutliLine = (EditText) findViewById(R.id.edtComplaint);
       btnComplaint = (Button) findViewById(R.id.btnsettingpost);

        loadSCaleData();

        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint  =  mutliLine.getText().toString();
                  FirebaseDatabase.getInstance().getReference("Complaints").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(complaint);

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL , new String[]{"shayur@easygames.co.za"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT , "Complaint  - " +FirebaseAuth.getInstance().getCurrentUser().getEmail()) ;
                emailIntent.putExtra(Intent.EXTRA_TEXT , complaint);
                emailIntent.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(emailIntent , "Select a Email Client"));

                }catch(android.content.ActivityNotFoundException ex){
                    Toast.makeText(Settings.this, "No Email client found!!",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });


        increaseAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scaleData + 0.1f >= 1.6f){

                }else{
                    scaleData = scaleData + 0.1f ;
                    updateScale(scaleData);
                    saveScale(scaleData);
                    edtFontDisp.setText(scaleData + "");

                }
            }
        });


        decreaseAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((scaleData - 0.1f) <= 0f){

                }else{
                    scaleData = scaleData - 0.1f ;
                    updateScale(scaleData);
                    saveScale(scaleData);
                    edtFontDisp.setText(scaleData + "");

                }
            }
        });




        edtFontDisp.setText(scaleData + "");

        ChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailDialog();
            }
        });
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordDialog();
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
    private void openPasswordDialog(){


        passwordDialog aPassword = new passwordDialog() ;
        aPassword.show(getSupportFragmentManager(),"username Dialog");


    }

    private void openDialog() {

        LanguageDialog lanDia = new LanguageDialog();
        lanDia.show(getSupportFragmentManager(),"Language Dialog"); 

    }


    private float scaleData ;

    private void loadSCaleData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_CODE , MODE_PRIVATE);
        scaleData = sharedPreferences.getFloat(SCALE , 1.0f);
        updateScale(scaleData);

    }
    public void saveScale(float ic){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_CODE , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(SCALE , ic );


        editor.apply();



    }

    private void updateScale(float inc){


        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.fontScale = inc ;

        res.updateConfiguration(config,dm);

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

    private String uNames  ;

    @Override
    public void ApplyUsername(String uName) {
        uNames = uName ;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref =  database.getReference("users");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child : snapshot.getChildren()){

                    User pulledUser =  child.getValue(User.class);
                    if(pulledUser.getEmail().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){

                        FirebaseDatabase.getInstance().getReference().child("users").child(pulledUser.getKey()).child("username").setValue(uNames);


                        SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userDetails.edit();
                        editor.putString("username", uNames);
                        editor.apply();

                        Toast.makeText(Settings.this , "Success" , Toast.LENGTH_SHORT).show();



                    }




                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





       // FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("username").setValue(uName);








    }

    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference myRef  =  fdb.getReference("users");
    String previousEmial ,currentEmail ;

    @Override
    public void ApplyEmail(String email) {

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(Settings.this, "Please provide a valid email", Toast.LENGTH_SHORT).show();


        }else{
            currentEmail =  email ;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            previousEmial = user.getEmail();
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Settings.this, "You have changed your password", Toast.LENGTH_SHORT).show();

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for(DataSnapshot child : snapshot.getChildren()){
                                        User a = child.getValue(User.class);

                                        if (a.getEmail().equalsIgnoreCase((previousEmial))){

   FirebaseDatabase.getInstance().getReference().child("users").child(a.getKey()).child("email").setValue(currentEmail);



                                        }


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }
                }
            });





        }



    }

    @Override
    public void ApplyUsername(String oldPass, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if(oldPass.equals(newPassword)){
            user.updatePassword(oldPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Settings.this
                            ,"You have succesfully changed your password",Toast.LENGTH_SHORT).show();

                        }
                }
            });



        }else{

            Toast.makeText(Settings.this
                    ,"Passwords do not match",Toast.LENGTH_SHORT).show();

        }


    }
}