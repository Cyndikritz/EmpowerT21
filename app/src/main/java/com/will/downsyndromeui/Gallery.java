package com.will.downsyndromeui;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Admin.ImageAdpter;
import Admin.Upload;

public class Gallery extends AppCompatActivity implements ImageAdpter.OnItemClickListener{

    //IMAGE PICKER---------------------------------------
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageURI;
    StorageTask uploadTask;
    StorageReference fbStorage;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    String currentUser;
    //---------------------------------------------------

    //GALLERY--------------------------------------------
    private static final int NUM_COLUMNS = 2;

    RecyclerView rcView;
    ImageAdpter imageAdapter;

    FirebaseStorage mStorage;
    DatabaseReference dbReference;
    ValueEventListener dbListener;

    List<Upload> uploads;
    ProgressBar prgbProgress;
    //---------------------------------------------------

    FloatingActionButton btnAddImage;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        btnAddImage = findViewById(R.id.btnAddImage);
        prgbProgress = findViewById(R.id.prgb_progress);
        btnBack = findViewById(R.id.btnBack);

        rcView = findViewById(R.id.recyclerView);
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getEmail(); //Getting the users email

        staggeredRecyclerView(uploads);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gallery.this, Dashboard.class);
                startActivity(intent);
            }
        });
        //CHECK
        imageAdapter.setOnItemClickListener((ImageAdpter.OnItemClickListener) Gallery.this);

        mStorage = FirebaseStorage.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("gallery");

        dbListener = dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploads.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if(upload.getmPublisher().equalsIgnoreCase(currentUser))
                    {
                        upload.setMkey(postSnapshot.getKey()); //saves unique key
                        uploads.add(upload);
                    }
                }

                //mAdapter.notifyDataSetChanged();
                imageAdapter.notifyDataSetChanged();
                prgbProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Gallery.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                prgbProgress.setVisibility(View.INVISIBLE);
            }
        });

        fbStorage = FirebaseStorage.getInstance().getReference(currentUser);
        dbRef = FirebaseDatabase.getInstance().getReference("gallery");

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take to new activity
                Intent intent = new Intent(Gallery.this, UploadGalleryImage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    //-------------------------Attribution--------------------------
    //The following code was derived from YouTube
    //Author: Coding In Flow
    //Link: https://www.youtube.com/watch?v=n2vcTdUpsLI
    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploads.get(position); //Gets the object of the selected image
        final String selectedKey = selectedItem.getMkey(); //Gets the key of that image

        //Deleting the image that was selected
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbReference.child(selectedKey).removeValue(); //Removing the image from storage
                Toast.makeText(Gallery.this, "Image Deleted", Toast.LENGTH_SHORT).show(); //Informing the user that the image has been deleted
            }
        });
    }
    //------------------------------End-----------------------------

    @Override
    protected void onDestroy(){
        super.onDestroy();

        dbReference.removeEventListener(dbListener);
    }

    //Method that receives a list of images and formats them to be displayed
    private void staggeredRecyclerView(List<Upload> uploads)
    {
        imageAdapter = new ImageAdpter(this, uploads);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        rcView.setLayoutManager(staggeredGridLayoutManager);
        rcView.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Gallery.this, Dashboard.class);
        startActivity(intent);
    }
}