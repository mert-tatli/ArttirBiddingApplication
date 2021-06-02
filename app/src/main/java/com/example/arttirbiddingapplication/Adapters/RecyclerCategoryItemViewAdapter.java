package com.example.arttirbiddingapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arttirbiddingapplication.Models.Category;
import com.example.arttirbiddingapplication.Interfaces.OnCategoryItemListener;
import com.example.arttirbiddingapplication.R;

import java.util.ArrayList;

public class RecyclerCategoryItemViewAdapter extends RecyclerView.Adapter<RecyclerCategoryItemViewAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private Context context;
    OnCategoryItemListener onCategoryItemListener;
    public RecyclerCategoryItemViewAdapter(Context context,ArrayList<Category> categories,OnCategoryItemListener onCategoryItemListener) {
        this.categories=categories;
        this.context = context;
        this.onCategoryItemListener=onCategoryItemListener;
    }

    @NonNull
    @Override
    public RecyclerCategoryItemViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category,parent,false);
        return new RecyclerCategoryItemViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCategoryItemViewAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(categories.get(position).getImageUrl())
                .into(holder.itemImage);

        holder.catName.setText(categories.get(position).getCategoryName());

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryItemListener.OnCategoryClick(categories.get(position).getCategoryName());


            }
        });


    }
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView catName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.categoryImage);
            catName=itemView.findViewById(R.id.categoryName);
        }
    }
}
