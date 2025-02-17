package Admin;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.will.downsyndromeui.Gallery;
import com.will.downsyndromeui.R;

import java.util.List;

public class ImageAdpter extends RecyclerView.Adapter<ImageAdpter.ImageHolder> {
        private Context mContext;
        private List<Upload> mUploads;
        private OnItemClickListener mListener;
        private View v;

        public ImageAdpter(Context context, List<Upload> uploads){
            mContext= context;
            mUploads=uploads;
        }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext.getClass().equals(Gallery.class)){
            v = LayoutInflater.from(mContext).inflate(R.layout.gallery_item,parent,false);
        }else{
            v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        }

        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Upload uploadCurrent= mUploads.get(position);

        holder.textViewName.setText(uploadCurrent.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Sets a placeholder image until the image has loaded
        //Using Glide to load the images into the cards
        Glide.with(mContext).load(uploadCurrent.getImageUrl()).apply(requestOptions).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageHolder extends  RecyclerView.ViewHolder implements View.OnClickListener ,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

                textViewName= itemView.findViewById(R.id.text);
            if(mContext.getClass().equals(Gallery.class)){
                imageView= itemView.findViewById(R.id.imgView);
            }else{
                imageView= itemView.findViewById(R.id.workpls);
            }
                itemView.setOnClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an Action");
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                switch (item.getItemId()){
                    case 1:
                        mListener.onDeleteClick(position);
                        return true;
                }
                }
            } return false;
        }
    }

    public interface OnItemClickListener{
            void onItemClick(int position);
            void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
