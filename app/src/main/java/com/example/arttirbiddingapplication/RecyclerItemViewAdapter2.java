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

public class RecyclerItemViewAdapter2 extends RecyclerView.Adapter<RecyclerItemViewAdapter2.ViewHolder> {

    private ArrayList<String> mImageUrls=new ArrayList<>();
    private Context context;

    public RecyclerItemViewAdapter2(Context context, ArrayList<String> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerItemViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bid_images,parent,false);
        return new RecyclerItemViewAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerItemViewAdapter2.ViewHolder holder, int position) {


        Glide.with(context)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.itemImage);


    }



    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.itemImages);

        }
    }
}
