package Admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.will.downsyndromeui.R;

import java.util.ArrayList;
import java.util.List;


public class UploadStoryForAdmin extends Fragment implements ImageAdpter.OnItemClickListener {
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonChooseImage;
    private Button mButtonUpload,mlist;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename, txtDescription;
    private CheckBox mEnglish, mAfrikaans;
    private RecyclerView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    StorageReference fileReference;
    private ImageAdpter mAdapter;
    private List<Upload> mUploads;
    private ImageView imgSelected;
    private List<Story> storyImages = new ArrayList<>();
    boolean checker = false;
    String title = "", description, language = "";
    int noInStory, pos = 0;

    public UploadStoryForAdmin() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return inflater.inflate(R.layout.fragment_upload_story_for_admin, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mlist=view.findViewById(R.id.list);
        mButtonChooseImage= view.findViewById(R.id.btnBackD);
        mButtonUpload= view.findViewById(R.id.button2);
        mTextViewShowUploads= view.findViewById(R.id.textView);
        mEditTextFilename= view.findViewById(R.id.editText);
        mImageView= view.findViewById(R.id.image_view);
        imgSelected = view.findViewById(R.id.imgSelected);
        mEnglish = view.findViewById(R.id.chkEnglish);
        mAfrikaans = view.findViewById(R.id.chkAfrikaans);
        txtDescription = view.findViewById(R.id.txtDescription);
        mImageView.setHasFixedSize(true);
        mImageView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploads = new ArrayList<>();
        mProgressBar= view.findViewById(R.id.progress_bar);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");//folder name
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("stories");//database name

        mEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mEnglish.isChecked()){
                    language = "English";
                    mAfrikaans.setChecked(false);
                }
            }
        });

        mAfrikaans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mAfrikaans.isChecked()){
                    language = "Afrikaans";
                    mEnglish.setChecked(false);
                }
            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hold =mEditTextFilename.getText().toString();
                if (hold.matches("") || checker==false){
                    Toast.makeText(getActivity(), "Add image and title/description", Toast.LENGTH_SHORT).show();
                }
                else{
                    addImage();
                }

            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mEditTextFilename.getText().toString().trim();
                if(!title.equalsIgnoreCase("") && !language.equalsIgnoreCase("")){
                    uploadfile();
                }else{
                    Toast.makeText(getContext(), "Please ensure your story has a title and specified language.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opemImageView();
            }
        });
    }

    private void opemImageView() {
        Intent intent = new Intent(getActivity(), images.class);
        startActivity(intent);
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST
                && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.get().load(mImageUri).into(imgSelected);
            imgSelected.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
            checker=true;
        }
    }
    private String getFileExtension(Uri uri){//might delete
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));

    }


    private void displayImages(){
        mAdapter = new ImageAdpter(getActivity(), mUploads);

        if (mAdapter == null) {
            Toast.makeText(getActivity(), "Oops, an error occurred.", Toast.LENGTH_SHORT).show();
        }
        mAdapter.setOnItemClickListener(this);
        mImageView.setAdapter(mAdapter);
    }
    private void addImage(){
        if (mImageUri!= null) {
            Upload holder= new Upload(txtDescription.getText().toString().trim(),mImageUri.toString(),"");
            Toast.makeText(getActivity(), "Image added", Toast.LENGTH_SHORT).show();
            mUploads.add(holder);
            imgSelected.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            txtDescription.setText("");
            mImageUri=null;
            checker=false;
            displayImages();
        }

        else{
            Toast.makeText(getActivity(), "No picture added", Toast.LENGTH_SHORT).show();
        }
    }

    private  void uploadfile() {

        if (mUploads.size()!=0){
            pos=0;
        for (final Upload list : mUploads) {
            fileReference = mStorageRef.child("uploads").child(Uri.parse(list.getImageUrl()).getLastPathSegment());

            fileReference.putFile(Uri.parse(list.getImageUrl())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            Uri downloadUrl = uris;
                            Story addImage = new Story(list.getName(), uris.toString(), pos);
                            storyImages.add(addImage);

                            if(pos == mUploads.size()-1){
                                String key = mDatabaseRef.child("stories").push().getKey();
                                Upload upload = new Upload(title, storyImages, language, key);
                                mDatabaseRef.child(key).setValue(upload);

                                Toast.makeText(getActivity(), "Story Uploaded", Toast.LENGTH_SHORT).show();
                                mEditTextFilename.setText("");
                                mEnglish.setChecked(false);
                                mAfrikaans.setChecked(false);
                            }
                            pos++;
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload " + e, Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });
        }
    }
        else{
            Toast.makeText(getActivity(), " Please add your story to the list first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        mUploads.remove(position);
        displayImages();

    }
}
