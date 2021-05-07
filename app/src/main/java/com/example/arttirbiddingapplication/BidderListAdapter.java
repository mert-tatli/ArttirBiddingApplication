package com.example.arttirbiddingapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class BidderListAdapter extends ArrayAdapter<BidderList> {
    private final Context context;
    private final List<BidderList> userList;

    public BidderListAdapter(@NonNull Context context, int resource, @NonNull List<BidderList> objects) {
        super(context, resource, objects);
        userList = objects;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.assignment_dialog_list_layout, parent, false);
        ImageView profilePic = rowView.findViewById(R.id.iv_user_profile_image);
        TextView userName = rowView.findViewById(R.id.tv_user_name);
        TextView bidPrice=rowView.findViewById(R.id.tv_user_bid);
        BidderList user = userList.get(position);

        userName.setText(user.getBidderName());

        bidPrice.setText("Arttırdığı Fiyat: "+user.getBidPrice()+"TL");

        String  profile = user.getUserProfile();

        Glide.with(context)
                .asBitmap()
                .load(profile)
                .into(profilePic);

        return rowView;
    }

}