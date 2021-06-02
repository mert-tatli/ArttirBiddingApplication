package com.example.arttirbiddingapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arttirbiddingapplication.Activities.BidActivity2;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class RecyclerBiddingFinishedItemAdapter extends RecyclerView.Adapter<RecyclerBiddingFinishedItemAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    public RecyclerBiddingFinishedItemAdapter(Context context,ArrayList<Product> products) {
        this.products=products;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerBiddingFinishedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_myauctions,parent,false);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        return new RecyclerBiddingFinishedItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerBiddingFinishedItemAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(products.get(position).getPictures().get(0))
                .into(holder.itemImage);

        holder.title.setText(products.get(position).getTitle());

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent=new Intent(context, BidActivity2.class);
                intent.putExtra("productId",products.get(position).getProductId());
                intent.putExtra("title",products.get(position).getTitle());
                intent.putExtra("details",products.get(position).getDetails());
                intent.putExtra("condition",products.get(position).isCondition());
                intent.putExtra("startingPrice",products.get(position).getStartingPrice());
                intent.putExtra("sellerId",products.get(position).getSellerId());
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
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.producttitle);
            itemImage=itemView.findViewById(R.id.producturl);
        }
    }



}
