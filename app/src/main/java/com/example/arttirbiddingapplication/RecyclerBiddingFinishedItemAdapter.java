package com.example.arttirbiddingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerBiddingFinishedItemAdapter extends RecyclerView.Adapter<RecyclerBiddingFinishedItemAdapter.ViewHolder>{


    private ArrayList<String> mImageUrls=new ArrayList<>();
    private Context context;

    public RecyclerBiddingFinishedItemAdapter(Context context,ArrayList<String> mImageUrls) {
        this.mImageUrls = mImageUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerBiddingFinishedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_biddingfinished,parent,false);
        return new RecyclerBiddingFinishedItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerBiddingFinishedItemAdapter.ViewHolder holder, int position) {


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
            itemImage=itemView.findViewById(R.id.finishedbiddingItemImage);

        }
    }
}
