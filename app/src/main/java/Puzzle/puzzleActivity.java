package Puzzle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.will.downsyndromeui.Dashboard;
import com.will.downsyndromeui.R;

import java.util.ArrayList;
import java.util.Random;

public class puzzleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static  int col ;
    private static  int dim, img;
    String[] Name = {"Apple","Avengers","Dog","Frog"};
    int[] Images = {R.drawable.apple,R.drawable.avengers,R.drawable.dog,R.drawable.frog};
    public static  Spinner spin;
    private static String[] blockLists;
    private static GestureDetectGridView mGridV;
    private static int mColWidth , mColHeight;
    int position;
    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";
    private static Bitmap bitmap;
    private  static ArrayList<Integer> bitmaps;
    private Button btnBack;
    private ImageView imgHelp;
    String name;

    public static ArrayList<puzzelCheck> pCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

       // img = getIntent().getIntExtra("img", 3);
        //col = getIntent().getIntExtra("int", 0);
        btnBack = findViewById(R.id.btnBackD);
        imgHelp = findViewById(R.id.imgHelpMain);
        position = getIntent().getIntExtra("Position",0);
        bitmaps = new ArrayList<>();
        bitmaps.add(R.drawable.apple);
        bitmaps.add(R.drawable.avengers);
        bitmaps.add(R.drawable.dog);
        bitmaps.add(R.drawable.frog);

        TextView text = findViewById(R.id.txtNamePuzzle);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(puzzleActivity.this, PuzzleMain.class);
                startActivity(intent);
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(puzzleActivity.this, "Swipe a piece left or right to swap it with the next one!", Toast.LENGTH_LONG).show();
            }
        });

        for(int i = 0; i< bitmaps.size();i++){
            if(i == position){
                bitmap = BitmapFactory.decodeResource(this.getResources(), bitmaps.get(i));
                name = Name[i];
                text.setText(name);
                break;
            }
        }

        spin = findViewById(R.id.colselect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.col, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        pCheck = new ArrayList<puzzelCheck>(getIntent().getIntExtra("arraylist",0));
        if( pCheck != null ){
            for(puzzelCheck p: pCheck ) {

                if(p.getPuzzelName() == name && p.getTwoByTwo() == true && p.getThreeByThree() == false && p.getFourByFour() == false){
                    spin.setSelection(2);

                }else if (p.getPuzzelName() == name && p.getTwoByTwo() == true && p.getThreeByThree() == true && p.getFourByFour() == false){
                    spin.setSelection(1);

                }else if (p.getPuzzelName() == name && p.getTwoByTwo() == true && p.getThreeByThree() == true && p.getFourByFour() == true){
                    spin.setSelection(2);

                }
            }
        }else{
            pCheck.add(0, new puzzelCheck(name, true, false, false));
            spin.setSelection(0);
        }



    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, text,Toast.LENGTH_LONG).show();
        if(position==0){
            col = 2;
            dim = col*col;
            init();
            jumbleBoard();
            setDim();
        } else if(position==1){
            col=3;
            dim = col*col;
            init();
            jumbleBoard();
            setDim();
        } else if(position==2){
            col = 4;
            dim = col*col;
            init();
            jumbleBoard();
            setDim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private  void setDim() {
        ViewTreeObserver vto = mGridV.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridV.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridV.getMeasuredWidth();
                int displayHeight = mGridV.getMeasuredHeight();
                int statusbarhieght = getStatusBarH(getApplicationContext());
                int requiredHeight = displayHeight - statusbarhieght;

                mColWidth = displayWidth / col;
                mColHeight = requiredHeight / col;
                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarH(Context context){
        int result = 0;
        int resourceID = context.getResources().getIdentifier("status_bar_height","dimen","android");

        if (resourceID > 0){
            result = context.getResources().getDimensionPixelOffset(resourceID);
        }
        return  result;
    }




    private void jumbleBoard() {
        int index;
        String temp;
        Random rand  = new Random();
        for(int i = blockLists.length-1; i>0; i--){

            index = rand.nextInt(i + 1);
            temp = blockLists[index];
            blockLists[index] = blockLists[i];
            blockLists[i] = temp;
        }
    }
    private void init() {
        mGridV = findViewById(R.id.grdImage);
        mGridV.setNumColumns(col);
        blockLists = new String[dim];
        for(int i = 0; i<dim; i++){
            blockLists[i] = String.valueOf(i);
        }
    }
    private static void swap(Context context, int currentPosition, int swap) {
        String newPosition = blockLists[currentPosition + swap];
        blockLists[currentPosition + swap] = blockLists[currentPosition];
        blockLists[currentPosition] = newPosition;
        display(context);

        if (isSolved()) {
            Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, PuzzleMain.class);
            if(spin.getSelectedItemPosition() == 0){
                i.putExtra("arraylist", pCheck);
            }
            context.startActivity(i);
        }
    }
    public static void moveBlock(Context context, String direction, int position) {

        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, col);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < col - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, col);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == col - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, col);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > col - 1 && position < dim - col &&
                position % col == 0) {
            if (direction.equals(up)) swap(context, position, -col);
            else if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, col);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == col * 2 - 1 || position == col * 3 - 1) {
            if (direction.equals(up)) swap(context, position, -col);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= dim - col - 1) swap(context, position,
                        col);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == dim - col) {
            if (direction.equals(up)) swap(context, position, -col);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < dim - 1 && position > dim - col) {
            if (direction.equals(up)) swap(context, position, -col);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(up)) swap(context, position, -col);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context, position, col);
        }
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < blockLists.length; i++) {
            if (blockLists[i].equals(String.valueOf(i))) {
                solved = true;

            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    public void home(){
        Intent i = new Intent(this, PuzzleMain.class);
        startActivity(i);
    }
    private static void display(Context context) {
        if (col == 2){
            P2(context,col,col,img,bitmap);
        }
        else if (col == 3){
           P3(context,col,col,img,bitmap);
        }
        else if (col == 4){
            P4(context,col,col,img,bitmap);
        }

    }

    private static void P2(Context context, int xCount, int yCount, int img, Bitmap bitm){
        ArrayList<Button> btnn = new ArrayList<>();
        Button btn ;

        Bitmap[][] imgs = new Bitmap[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitm.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitm.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                imgs[x][y] = Bitmap.createBitmap(bitm, x * width, y * height, width, height);
            }
        }



        for(int i = 0; i<blockLists.length; i++) {
            btn = new Button(context);
            if (blockLists[i].equals("0"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][0]));
            else if (blockLists[i].equals("1"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][0]));
            else if (blockLists[i].equals("2"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][1]));
            else if (blockLists[i].equals("3"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][1]));

            btnn.add(btn);


        }mGridV.setAdapter(new CustomAdapter(btnn,mColWidth,mColHeight));
    }

   private static void P3(Context context,int xCount, int yCount, int img,Bitmap bitm){
        ArrayList<Button> btnn = new ArrayList<>();
        Button btn ;

       Bitmap[][] imgs = new Bitmap[xCount][yCount];
       int width, height;
       // Divide the original bitmap width by the desired vertical column count
       width = bitm.getWidth() / xCount;
       // Divide the original bitmap height by the desired horizontal row count
       height = bitm.getHeight() / yCount;
       // Loop the array and create bitmaps for each coordinate
       for(int x = 0; x < xCount; ++x) {
           for(int y = 0; y < yCount; ++y) {
               // Create the sliced bitmap
               imgs[x][y] = Bitmap.createBitmap(bitm, x * width, y * height, width, height);
           }
       }
        for(int i = 0; i<blockLists.length; i++){
            btn = new Button(context);
            if(blockLists[i].equals("0"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][0]));
            else if(blockLists[i].equals("1"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][0]));
            else if(blockLists[i].equals("2"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][0]));
            else if(blockLists[i].equals("3"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][1]));
            else if(blockLists[i].equals("4"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][1]));
            else if(blockLists[i].equals("5"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][1]));
            else if(blockLists[i].equals("6"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][2]));
            else if(blockLists[i].equals("7"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][2]));
            else if(blockLists[i].equals("8"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][2]));

            btnn.add(btn);
        }

    mGridV.setAdapter(new CustomAdapter(btnn,mColWidth,mColHeight));


    }private static void P4(Context context,int xCount, int yCount, int img,Bitmap bitm){
        ArrayList<Button> btnn = new ArrayList<>();
        Button btn ;


        Bitmap[][] imgs = new Bitmap[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitm.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitm.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                imgs[x][y] = Bitmap.createBitmap(bitm, x * width, y * height, width, height);
            }
        }
        for(int i = 0; i<blockLists.length; i++){
            btn = new Button(context);
            if(blockLists[i].equals("0"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][0]));
            else if(blockLists[i].equals("1"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][0]));
            else if(blockLists[i].equals("2"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][0]));
            else if(blockLists[i].equals("3"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[3][0]));
            else if(blockLists[i].equals("4"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][1]));
            else if(blockLists[i].equals("5"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][1]));
            else if(blockLists[i].equals("6"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][1]));
            else if(blockLists[i].equals("7"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[3][1]));
            else if(blockLists[i].equals("8"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][2]));
            else if(blockLists[i].equals("9"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][2]));
            else if(blockLists[i].equals("10"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][2]));
            else if(blockLists[i].equals("11"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[3][2]));
            else if(blockLists[i].equals("12"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[0][3]));
            else if(blockLists[i].equals("13"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[1][3]));
            else if(blockLists[i].equals("14"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[2][3]));
            else if(blockLists[i].equals("15"))
                btn.setBackground(new BitmapDrawable(context.getResources(),imgs[3][3]));


            btnn.add(btn);
        }

        mGridV.setAdapter(new CustomAdapter(btnn,mColWidth,mColHeight));


    }


}
