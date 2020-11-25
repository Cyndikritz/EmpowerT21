package Puzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.will.downsyndromeui.Dashboard;
import com.will.downsyndromeui.PullPuzzles;
import com.will.downsyndromeui.R;

import java.util.ArrayList;
import java.util.List;

import Admin.Upload;

public class PuzzleMain extends AppCompatActivity {
    GridView gridView;

    String[] Name = {"Apple","Avengers","Dog","Frog"};
    int[] Images = {R.drawable.apple,R.drawable.avengers,R.drawable.dog,R.drawable.frog};
    List<String> title = new ArrayList<>();
    List<String> images = new ArrayList<>();
    Button btnpz, btnBack;
    ImageView imgHelp;
    ArrayList<puzzelCheck> pCheck;
    PullPuzzles pulledPuzzles = new PullPuzzles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);

        for(Upload list: pulledPuzzles.getPuzzles()){
            title.add(list.getName());
            images.add(list.getImageUrl());
        }

        pCheck = new ArrayList<puzzelCheck>(getIntent().getIntExtra("arraylist",0));

        btnBack = findViewById(R.id.btnBack);
        imgHelp = findViewById(R.id.imgHelp);
        gridView = findViewById(R.id.gridview);
        CustAdapterGallary custAdapterGallary = new CustAdapterGallary();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuzzleMain.this, Dashboard.class);
                startActivity(intent);
            }
        });

        gridView.setAdapter(custAdapterGallary);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), puzzleActivity.class);

                if(pCheck != null){
                    intent.putExtra("arraylist", pCheck);

                }
                intent.putExtra("Position" , position);

                intent.putExtra("img",0);

                startActivity(intent);
            }
        });
    }

    private  class CustAdapterGallary extends BaseAdapter {

        @Override
        public int getCount() {
            return Images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.data, null);

            TextView name = view.findViewById(R.id.name);
            ImageView imageView = view.findViewById(R.id.images);



            name.setText(Name[position]);
            imageView.setImageResource(Images[position]);
            //Glide.with(PuzzleMain.this).load(images.get(position)).into(imageView);
            return view;
        }
    }



}
