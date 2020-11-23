package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.will.downsyndromeui.R;


public class testQuestionFrag extends Fragment {
    public static final int PICK_IMAGE_REQUEST=1;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFilename;
    private Spinner mLevel;
    private CheckBox mEnglish, mAfrikaans;
    private ProgressBar mProgressBar;
    private DatabaseReference mDatabaseRef;
    String language;
    int level = 99;

    public testQuestionFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_question, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mButtonUpload= view.findViewById(R.id.btnAddSentence);
        mTextViewShowUploads= view.findViewById(R.id.textView3);
        mEditTextFilename= view.findViewById(R.id.txtSentence);
        mProgressBar= view.findViewById(R.id.progsSentence);
        mLevel = view.findViewById(R.id.spnLevelList);
        mEnglish = view.findViewById(R.id.chkEnglish);
        mAfrikaans = view.findViewById(R.id.chkAfrikaans);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.levels, R.layout.action_item_list_small);
        adapter.setDropDownViewResource(R.layout.action_item_list_small);
        mLevel.setAdapter(adapter);

        if(mEnglish.isChecked()){
            language = "English";
            mAfrikaans.setChecked(false);
        }else{
            language = "Afrikaans";
            mEnglish.setChecked(false);
        }

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

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("sentences");//database name

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String hold= mEditTextFilename.getText().toString();
                if (hold.matches("")) {
                    Toast.makeText(getActivity(), "Please enter a sentence", Toast.LENGTH_SHORT).show(); }
                if(level == 99) {
                    Toast.makeText(getContext(), "Please select an appropriate level.", Toast.LENGTH_SHORT).show();
                }else if(!mEnglish.isChecked() && !mAfrikaans.isChecked()) {
                    Toast.makeText(getContext(), "Please select an appropriate language.", Toast.LENGTH_SHORT).show();
                }else{
                    uploadfile();
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
    public void uploadfile(){
        String key=mDatabaseRef.child("sentences").push().getKey();
        TestClass upload = new TestClass(mEditTextFilename.getText().toString(),key, level);
        mDatabaseRef.child(key).setValue(upload);
        Toast.makeText(getActivity(), "Sentence Uploaded.", Toast.LENGTH_SHORT).show();
    }
    private void openImageView() {
        Intent intent = new Intent(getActivity(), questions1.class);
        startActivity(intent);

    }
}
