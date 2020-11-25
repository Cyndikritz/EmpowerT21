package com.will.downsyndromeui;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Admin.ImagesforAdmin;
import Admin.Upload;
import Puzzle.PuzzleMain;

public class PullPuzzles {
    private List<Upload> puzzles = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    public PullPuzzles(){

    }

    public List<Upload> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(List<Upload> puzzles) {
        this.puzzles = puzzles;
    }

    public void FetchPuzzles(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("puzzles");
        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                puzzles.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setMkey(postSnapshot.getKey());
                    puzzles.add(upload);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
