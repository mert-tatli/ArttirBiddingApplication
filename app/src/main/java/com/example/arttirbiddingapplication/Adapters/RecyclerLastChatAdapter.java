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
import com.example.arttirbiddingapplication.Activities.ChatActivity;
import com.example.arttirbiddingapplication.Models.Chat;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.Models.User;
import com.example.arttirbiddingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class RecyclerLastChatAdapter extends RecyclerView.Adapter<RecyclerLastChatAdapter.ViewHolder> {

    private ArrayList<User> users;
    private Context context;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    public RecyclerLastChatAdapter(Context context,ArrayList<User> users) {
        this.users=users;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerLastChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_last_chat,parent,false);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        return new RecyclerLastChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerLastChatAdapter.ViewHolder holder, int position) {


      if (users.get(position).getProfileImage().equals(""))
      {
          Glide.with(context)
                  .asBitmap()
                  .load(R.drawable.profile_image)
                  .into(holder.itemImage);
      }
      else {
          Glide.with(context)
                  .asBitmap()
                  .load(users.get(position).getProfileImage())
                  .into(holder.itemImage);
      }

        holder.name.setText(users.get(position).getUserName()+" "+users.get(position).getUserSurname());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId",users.get(position).getUserId());
                intent.putExtra("userName",users.get(position).getUserName()+" "+users.get(position).getUserSurname());
                context.startActivity(intent);
                CustomIntent.customType(context,"left-to-right");
            }
        });

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId",users.get(position).getUserId());
                intent.putExtra("userName",users.get(position).getUserName()+" "+users.get(position).getUserSurname());
                context.startActivity(intent);
                CustomIntent.customType(context,"left-to-right");

            }
        });

    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userNameFrag);
            itemImage=itemView.findViewById(R.id.imageViewUser);
        }
    }



}
