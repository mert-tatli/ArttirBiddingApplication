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
import com.example.arttirbiddingapplication.Activities.BidActivity;
import com.example.arttirbiddingapplication.Activities.BidActivity2;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import maes.tech.intentanim.CustomIntent;

public class RecyclerProductViewAdapter extends RecyclerView.Adapter<RecyclerProductViewAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public RecyclerProductViewAdapter(Context context,ArrayList<Product> products) {
        this.products=products;
        this.context = context;


    }

    @NonNull
    @Override
    public RecyclerProductViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_auction,parent,false);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        return new RecyclerProductViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductViewAdapter.ViewHolder holder, int position) {


        Glide.with(context)
                .asBitmap()
                .load(products.get(position).getPictures().get(0))
                .into(holder.itemImage);

        holder.title.setText(products.get(position).getTitle());

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (fAuth.getCurrentUser()==null)
                {
                    intent=new Intent(context, BidActivity.class);
                }

                else{
                     if (products.get(position).getSellerId().equals(fUser.getUid()) || holder.checkDates(products.get(position).getEndDate())) {
                        intent=new Intent(context, BidActivity2.class);
                    }
                     else {
                         intent=new Intent(context, BidActivity.class);
                     }

                }


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
            title=itemView.findViewById(R.id.product_title);
            itemImage=itemView.findViewById(R.id.product_url);
        }

        private boolean checkDates(String date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date event_date = null;
            try {
                event_date = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date current_date = new Date();
            if (current_date.after(event_date))
            {
                return true;
            }
            return false;
        }
    }



}
