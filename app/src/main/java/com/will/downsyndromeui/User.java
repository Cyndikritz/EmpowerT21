package com.will.downsyndromeui;

public class User {

    String username, email, password,
            name, surname,  parent;
    int level, levelXP, age;

    public User(){

    }

    public User(String username, String email, String password, String name, String surname,
                int age, String parent, int level, int levelXP) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.parent = parent;
        this.level = level;
        this.levelXP = levelXP;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
