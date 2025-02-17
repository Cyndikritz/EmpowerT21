package Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.will.downsyndromeui.R;

import java.util.ArrayList;
import java.util.List;

public class questions1 extends AppCompatActivity implements QuestionAdpter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private QuestionAdpter mAdapter;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    String TAG=" plz work";
    private List<TestClass> mUploads;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions1);
        mRecyclerView =findViewById(R.id.recyclerViewForQuestion);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads= new ArrayList<>();
        mAdapter= new QuestionAdpter(questions1.this,mUploads);
        mAdapter.setOnItemClickListener(questions1.this);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("sentences");

        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    if (postSnapshot==null) {
                        Toast.makeText(questions1.this, "There are no available sentences.", Toast.LENGTH_SHORT).show();
                    }else{
                    TestClass upload = postSnapshot.getValue(TestClass.class);
                    upload.setMkey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                }
                mAdapter.notifyDataSetChanged();
                if (mAdapter == null){
                    Toast.makeText(questions1.this, "No sentences added.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(questions1.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        TestClass selectedItem=mUploads.get(position);
        final String selectedkEY= selectedItem.getMkey();

                mDatabaseRef.child(selectedkEY).removeValue();
                Toast.makeText(questions1.this, "Sentence deleted.", Toast.LENGTH_SHORT).show();

    }
}
