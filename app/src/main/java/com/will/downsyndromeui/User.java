package com.will.downsyndromeui;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {

    private String username, email, name, surname,  parent, currentUser, key;
    private int level, levelXP, age;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    public User(){

    }

    public User(int age, String email, int level, int levelXP, String name, String parent,
                String surname, String username, String key) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.parent = parent;
        this.level = level;
        this.levelXP = levelXP;
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelXP() {
        return levelXP;
    }

    public void setLevelXP(int levelXP) {
        this.levelXP = levelXP;
    }

    public String getKey(){return key;}

    public void setKey(String key){this.key = key;}

}
