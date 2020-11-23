package Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.will.downsyndromeui.MainActivity;
import com.will.downsyndromeui.R;


public class MainActivityAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    UploadStoryForAdmin storyForAdmin;
    BlankFragment blankFragment;
    fragPuzzels FragPuzzels;
    testQuestionFrag TestQuestionFrag;
    TextView btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        mAuth = FirebaseAuth.getInstance();
        Spinner spinner = (Spinner) findViewById(R.id.spinners);
        btnLogout = findViewById(R.id.btnLogout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.holder_array, R.layout.action_item_list);
        adapter.setDropDownViewResource(R.layout.action_item_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        blankFragment= new BlankFragment();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences preferences = getSharedPreferences("remember", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();

                SharedPreferences userDetails = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = userDetails.edit();
                editor.putString("username", "");
                editor.putInt("level", 1);
                editor.putInt("XP", 0);
                editor.apply();
                Intent intent = new Intent(MainActivityAdmin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {


            case 0:
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flfragment, blankFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1: //move to UploadStory
                storyForAdmin= new UploadStoryForAdmin();
                final FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction2.replace(R.id.flfragment, storyForAdmin);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case 2://move to Puzzels
                FragPuzzels= new fragPuzzels();
                final FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction3.replace(R.id.flfragment, FragPuzzels);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case 3: //move to Words
                TestQuestionFrag= new testQuestionFrag();
                final FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                //frag1
                transaction4.replace(R.id.flfragment, TestQuestionFrag);
                transaction4.addToBackStack(null);
                transaction4.commit();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed(){

    }

}

