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
import com.example.arttirbiddingapplication.Models.Chat;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.Models.User;
import com.example.arttirbiddingapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class RecyclerMessagesAdapter extends RecyclerView.Adapter<RecyclerMessagesAdapter.ViewHolder> {

    private ArrayList<Chat> chatList;
    private Context context;
    private int MESSAGE_TYPE_LEFT = 0;
    private int MESSAGE_TYPE_RIGHT = 1;
    private String senderPhoto=""
            , receiverPhoto="";

    public RecyclerMessagesAdapter(Context context, ArrayList<Chat> chatList, String senderPhoto, String receiverPhoto) {
        this.chatList = chatList;
        this.context = context;
        this.senderPhoto = senderPhoto;
        this.receiverPhoto = receiverPhoto;

    }

    @NonNull
    @Override
    public RecyclerMessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MESSAGE_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left, parent, false);
            return new RecyclerMessagesAdapter.ViewHolder(view);

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
            return new RecyclerMessagesAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMessagesAdapter.ViewHolder holder, int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSenderId().equals(firebaseUser.getUid())) {
            if (receiverPhoto.isEmpty())
            {
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.profile_image)
                        .into(holder.itemImage);

            }
            else {
                Glide.with(context)
                        .asBitmap()
                        .load(receiverPhoto)
                        .into(holder.itemImage);
            }
        } else {
            if (senderPhoto.isEmpty())
            {
                Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.profile_image)
                    .into(holder.itemImage);

            }
            else {
                Glide.with(context)
                        .asBitmap()
                        .load(senderPhoto)
                        .into(holder.itemImage);
            }

        }


        holder.message.setText(chatList.get(position).getMessage());


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tvMessage);
            itemImage = itemView.findViewById(R.id.userImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSenderId().equals(firebaseUser.getUid())) {
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
    }


}
