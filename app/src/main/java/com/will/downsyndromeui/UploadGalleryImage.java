package com.will.downsyndromeui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import Admin.Upload;

public class UploadGalleryImage extends AppCompatActivity {

    //IMAGE PICKER---------------------------------------
    private static final int PICK_IMAGE_REQUEST = 1;
    StorageTask uploadTask;
    StorageReference fbStorage;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    String currentUser, title;
    boolean checker= false;
    Uri mImageUri=null;
    //---------------------------------------------------

    Button btnUpload, btnPicture, btnBack;
    EditText txtTitle;
    ImageView imgPicture;
    ProgressBar progsUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gallery_image);

        btnUpload = findViewById(R.id.btnUploadPicture);
        btnPicture = findViewById(R.id.btnAddPic);
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        imgPicture = findViewById(R.id.imgPicture);
        progsUpload = findViewById(R.id.progsUpload);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getEmail(); //Getting the users email

        fbStorage = FirebaseStorage.getInstance().getReference(currentUser);
        dbRef = FirebaseDatabase.getInstance().getReference("gallery");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadGalleryImage.this, Gallery.class);
                startActivity(intent);
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txtTitle.getText().toString().trim();
                if(!title.equalsIgnoreCase("") && checker != false){
                    uploadFile();
                }else{
                    Toast.makeText(UploadGalleryImage.this, "Please select a picture and give it a title.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method that will open a file chooser to allow the user to select an image
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*"); //Ensure only images are available to be selected
        intent.setAction(Intent.ACTION_GET_CONTENT); //Retrieves the selected image
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.get().load(mImageUri).into(imgPicture);
            checker=true;

        }
    }

    //Gets file extension
    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //Method that will upload the image to storage and store its url in the database
    private void uploadFile()
    {
        //-------------------------------Attribution---------------------------------
        //The following code was adapted from YouTube
        //Author: Coding In Flow
        //Link: https://www.youtube.com/watch?v=lPfQN-Sfnjw
        if(mImageUri != null)
        {
            final StorageReference fileReference = fbStorage.child(System.currentTimeMillis()+
                    "."+getFileExtension(mImageUri)); //Getting the storage reference

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //Informing the user that the upload has been successful
                            Toast.makeText(UploadGalleryImage.this,"Upload Successful", Toast.LENGTH_SHORT).show();
                            String key = dbRef.child("gallery").push().getKey();
                            //creating an object that contains the image name and its unique uri to its storage location
                            //Upload upload = new Upload(txtFileName.getText().toString().trim(), uri.toString());
                            Upload upload = new Upload(title,
                                    uri.toString(), currentUser, key);

                            dbRef.child(key).setValue(upload);

                            txtTitle.setText("");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadGalleryImage.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //Informing the user that their image is being uploaded
                    Toast.makeText(UploadGalleryImage.this, "Uploading Image... Please be patient", Toast.LENGTH_SHORT).show();

                    //get current transferred bytes compared to total bytes
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progsUpload.setProgress((int)progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
        //----------------------------------End--------------------------------------
    }
}