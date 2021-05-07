package com.example.arttirbiddingapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class RecyclerProductViewAdapter extends RecyclerView.Adapter<RecyclerProductViewAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;

    public RecyclerProductViewAdapter(Context context,ArrayList<Product> products) {
        this.products=products;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerProductViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_auction,parent,false);
        return new RecyclerProductViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductViewAdapter.ViewHolder holder, int position) {


        Glide.with(context)
                .asBitmap()
                .load(products.get(position).getPictures().get(0))
                .into(holder.itemImage);


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,BidActivity.class);
                intent.putExtra("productId",products.get(position).getProductId());
                intent.putExtra("title",products.get(position).getTitle());
                intent.putExtra("details",products.get(position).getDetails());
                intent.putExtra("condition",products.get(position).getCondition());
                intent.putExtra("increasingRate",products.get(position).getIncreasingRate());
                intent.putStringArrayListExtra("photos",products.get(position).getPictures());
                context.startActivity(intent);
                CustomIntent.customType(context,"bottom-to-up");

            }
        });



    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.product_url);
        }
    }



}
