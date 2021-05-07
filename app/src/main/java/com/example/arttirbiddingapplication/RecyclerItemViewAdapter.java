package com.example.arttirbiddingapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class RecyclerItemViewAdapter extends RecyclerView.Adapter<RecyclerItemViewAdapter.ViewHolder> {

    public static final String TAG="RecyclerItemViewAdapter";
    private ArrayList<Uri> mImageUrls=new ArrayList<>();
    private Context context;

    public RecyclerItemViewAdapter(Context context, ArrayList<Uri> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.itemImage);


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }



    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.itemImage);

        }
    }



}
