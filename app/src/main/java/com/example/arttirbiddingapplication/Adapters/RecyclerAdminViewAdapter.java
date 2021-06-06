package com.example.arttirbiddingapplication.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.arttirbiddingapplication.Activities.BidActivity3;
import com.example.arttirbiddingapplication.Interfaces.OnDeleteItemListener;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import maes.tech.intentanim.CustomIntent;

public class RecyclerAdminViewAdapter extends RecyclerView.Adapter<RecyclerAdminViewAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private Context context;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    OnDeleteItemListener onDeleteItemListener;

    public RecyclerAdminViewAdapter(Context context, ArrayList<Product> products, OnDeleteItemListener onDeleteItemListener) {
        this.products=products;
        this.context = context;
        this.onDeleteItemListener=onDeleteItemListener;
    }
    @NonNull
    @Override
    public RecyclerAdminViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_auction,parent,false);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        return new RecyclerAdminViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdminViewAdapter.ViewHolder holder, int position) {


        Glide.with(context)
                .asBitmap()
                .load(products.get(position).getPictures().get(0))
                .into(holder.itemImage);

        holder.title.setText(products.get(position).getTitle());

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(context, BidActivity3.class);
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

        holder.itemImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Onaylayın");
                builder.setMessage("Silmek istediğinizden emin misiniz?");

                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        onDeleteItemListener.OndeleteClick(products.get(position).getProductId(),products.get(position).getSellerId());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
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
    }
}
