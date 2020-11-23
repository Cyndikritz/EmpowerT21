package Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


public class fragPuzzels extends Fragment {
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonChooseImage;
    private Button mBtnFlashcard, mBtnPuzzle;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private ImageView mImageView;
    private Spinner mLevel, mCategory, spinner;
    private CheckBox mEnglish, mAfrikaans;
    private ProgressBar mProgressBar;
    private Uri mImageUri=null;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    StorageReference fileReference;
    private ImageAdpter mAdapter;
    boolean checker= false, isFlashcard = false;
    String language, category, holder;
    int level = 99;
    Upload upload;

    public fragPuzzels() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_puzzels, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonChooseImage= view.findViewById(R.id.btnAddPic);
        mBtnFlashcard = view.findViewById(R.id.btnUploadPicture);
        mBtnPuzzle = view.findViewById(R.id.btnUploadPuzzle);
        mTextViewShowUploads= view.findViewById(R.id.textView1);
        mEditTextFilename= view.findViewById(R.id.txtTitle);
        mImageView= view.findViewById(R.id.imgPicture);
        mProgressBar= view.findViewById(R.id.progsUpload);
        mLevel = view.findViewById(R.id.spnLevelList);
        mCategory = view.findViewById(R.id.spnCategory);
        mEnglish = view.findViewById(R.id.chkEnglish);
        mAfrikaans = view.findViewById(R.id.chkAfrikaans);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");//folder name
        spinner = (Spinner) view.findViewById(R.id.spinners);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, R.layout.action_item_list);
        adapter.setDropDownViewResource(R.layout.action_item_list_small);
        mCategory.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.levels, R.layout.action_item_list);
        adapter2.setDropDownViewResource(R.layout.action_item_list_small);
        mLevel.setAdapter(adapter2);

        if(mEnglish.isChecked()){
            language = "English";
            mAfrikaans.setChecked(false);
        }else{
            language = "Afrikaans";
            mEnglish.setChecked(false);
        }

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        category = "Numbers";
                        break;
                    case 2:
                        category = "Animals";
                        break;
                    case 3:
                        category = "Words";
                        break;
                    case 4:
                        category = "Colours and Shapes";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        level = 1;
                        break;
                    case 2:
                        level = 2;
                        break;
                    case 3:
                        level = 3;
                        break;
                    case 4:
                        level = 4;
                        break;
                    case 5:
                        level = 5;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mBtnFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashcard = true;
                holder =mEditTextFilename.getText().toString();
                if (holder.matches("") || checker==false){
                    Toast.makeText(getActivity(), "Please select an image and enter a title.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(category.equals("") || level == 99) {
                        Toast.makeText(getContext(), "Please select an appropriate category and level.", Toast.LENGTH_SHORT).show();
                    }else if(!mEnglish.isChecked() && !mAfrikaans.isChecked()) {
                        Toast.makeText(getContext(), "Please select an appropriate language.", Toast.LENGTH_SHORT).show();
                    }else
                        {
                            uploadfile();
                            mImageView.setImageResource(android.R.color.transparent);
                            mEditTextFilename.setText("");
                            mImageUri=null;
                            checker=false;
                        }
                    }
                }
        });

        mBtnPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashcard = false;
                holder =mEditTextFilename.getText().toString();

                if (holder.matches("") || checker==false){
                    Toast.makeText(getActivity(), "Please select an image and enter a title.", Toast.LENGTH_SHORT).show();
                }
                else if(!mEnglish.isChecked() && !mAfrikaans.isChecked()) {
                        Toast.makeText(getContext(), "Please select an appropriate language.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        uploadfile();
                        mImageView.setImageResource(android.R.color.transparent);
                        mEditTextFilename.setText("");
                        mImageUri=null;
                        checker=false;
                    }
                }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageView();
            }
        });
    }

    private void openImageView() {
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
            Picasso.get().load(mImageUri).into(mImageView);
            checker=true;

        }
    }
    private  void uploadfile(){
        if(isFlashcard){
            mDatabaseRef= FirebaseDatabase.getInstance().getReference("flashcards");//folder name
        }else{
            mDatabaseRef= FirebaseDatabase.getInstance().getReference("puzzles");//folder name
        }

            fileReference = mStorageRef.child("uploads").child(Uri.parse(mImageUri.toString()).getLastPathSegment());
            fileReference.putFile(Uri.parse(mImageUri.toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uris) {
                            Uri downloadUrl =uris ;
                            String key=mDatabaseRef.child("uploads").push().getKey();
                            if(!isFlashcard){
                                upload = new Upload(holder,
                                        uris.toString(), language, key);
                            }else {
                                upload = new Upload(holder,
                                        uris.toString(), level, language, key);
                            }
                            mDatabaseRef.child(key).setValue(upload);
                            Toast.makeText(getActivity(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            mLevel.setSelection(0);
                            mCategory.setSelection(0);
                            mEnglish.setChecked(false);
                            mAfrikaans.setChecked(false);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to upload. "+e, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress =(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int)progress);
                }
            });
    }

}
