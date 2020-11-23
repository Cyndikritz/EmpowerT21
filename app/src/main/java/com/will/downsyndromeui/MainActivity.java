package com.will.downsyndromeui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import Admin.MainActivityAdmin;

import static android.R.layout.simple_spinner_dropdown_item;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    Button login, signUp ;
    EditText txtUsername, txtPassword;
    String password, email;
    CheckBox chkRemember;
    ProgressBar progressBar;
    boolean check = false, rem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        txtUsername = findViewById(R.id.edtUsername);
        txtPassword = findViewById(R.id.edtPassword);
        chkRemember = findViewById(R.id.RemeberPass);
        progressBar = findViewById(R.id.progsLogin);
        progressBar.setVisibility(View.INVISIBLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER,
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER);

        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }else{
                    SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }


            }
        });

        signUp = findViewById(R.id.Register);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this , Register.class);
                startActivity(a);
            }
        });


         login  = findViewById(R.id.Login);
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                email = txtUsername.getText().toString().trim();
                password = txtPassword.getText().toString().trim();

                if(!email.isEmpty() && !password.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                if(email.equalsIgnoreCase("18000762@vcconnect.co.za")){
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    Intent a = new Intent(MainActivity.this , MainActivityAdmin.class);
                                                    startActivity(a);
                                                }else{
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    Intent a = new Intent(MainActivity.this , Dashboard.class);
                                                    startActivity(a);
                                                }
                                            }else{
                                                Toast.makeText(MainActivity.this, "Oops, something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                            // ...
                                        }
                                    });
                            return;
                }else{
                    Toast.makeText(MainActivity.this, "Please enter all details!", Toast.LENGTH_SHORT).show();
                }
             }
         });
    }

    @Override
    protected void onStart() {

        SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
        String start = preferences.getString("remember", "");
        if(mAuth.getCurrentUser() != null){
            if(start.equals("true")){
                if(mAuth.getCurrentUser().getEmail().equalsIgnoreCase("18000762@vcconnect.co.za")){
                    Intent intent = new Intent(MainActivity.this, MainActivityAdmin.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                }
            }
        }

        super.onStart();
    }

    @Override
    public void onBackPressed(){

    }
}
