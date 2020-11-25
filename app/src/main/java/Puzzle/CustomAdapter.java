package Puzzle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Button>mBtn;
    private int mColWidth, mColHeight;

    public CustomAdapter(ArrayList<Button> Btn, int colWidth, int colHeight) {
        mBtn = Btn;
        mColWidth = colWidth;
        mColHeight = colHeight;

    }



    @Override
    public int getCount() {
        return mBtn.size();
    }

    @Override
    public Object getItem(int position) {
        return (Object) mBtn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn;
        if(convertView == null){

            btn = mBtn.get(position);
        }else{
            btn = (Button) convertView;
        }

        android.widget.AbsListView.LayoutParams p = new android.widget.AbsListView.LayoutParams(mColWidth,mColHeight);

        btn.setLayoutParams(p);


        return btn;
    }
}
