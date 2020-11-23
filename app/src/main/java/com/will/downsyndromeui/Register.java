package com.will.downsyndromeui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button cancel, register;
    EditText txtUsername, txtEmail, txtParentN, txtParentS, txtName, txtSurname,
            txtPassword, txtConfirm, txtAge;
    String name, surname, username, parentN, parentS, email, password, confirm, uploadID;
    int age = 0;
    boolean isNumber = true, remember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        cancel = findViewById(R.id.btnCancel);
        register = findViewById(R.id.btnRegister);
        txtUsername = findViewById(R.id.edtUsername);
        txtEmail = findViewById(R.id.edtEmail);
        txtParentN = findViewById(R.id.edtParentName);
        txtParentS = findViewById(R.id.edtParentSurname);
        txtName = findViewById(R.id.edtName);
        txtSurname = findViewById(R.id.edtSurname);
        txtPassword = findViewById(R.id.edtPassword);
        txtConfirm = findViewById(R.id.edtRepass);
        txtAge = findViewById(R.id.edtAge);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString().trim();
                surname = txtSurname.getText().toString().trim();
                username = txtUsername.getText().toString().trim();
                parentN = txtParentN.getText().toString().trim();
                parentS = txtParentS.getText().toString().trim();
                email = txtEmail.getText().toString().trim();
                try{
                    age = Integer.parseInt(txtAge.getText().toString().trim());
                    isNumber = true;
                }catch(NumberFormatException e){
                    isNumber = false;
                }
                password = txtPassword.getText().toString().trim();
                confirm = txtConfirm.getText().toString().trim();

                if((isNumber == true && age > 0)){
                    if(password.equals(confirm)){
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                            // Sign in success, update UI with the signed-in user's information
                                            uploadID = myRef.push().getKey();
                                            User newUser = new User(age, email, 1, 0, name, parentN+" "+parentS, surname, username, uploadID);
                                            myRef.child(uploadID).setValue(newUser);

                                            SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = userDetails.edit();
                                            editor.putString("username", username);
                                            editor.putInt("level", 1);
                                            editor.putInt("XP", 0);
                                            editor.apply();

                                            Intent intent = new Intent(Register.this, Dashboard.class);
                                            intent.putExtra("child name", username);
                                            Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(Register.this, "Unable to create account. Please try again",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else{
                    Toast.makeText(Register.this, "Please make sure you have correctly entered all your details above",
                           Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
