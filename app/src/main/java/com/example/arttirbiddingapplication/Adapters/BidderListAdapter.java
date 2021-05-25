package com.example.arttirbiddingapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.arttirbiddingapplication.Models.Profile;
import com.example.arttirbiddingapplication.R;

import java.util.List;

public class BidderListAdapter extends ArrayAdapter<Profile> {
    private final Context context;
    private final List<Profile> userList;

    public BidderListAdapter(@NonNull Context context, int resource, @NonNull List<Profile> objects) {
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

        Profile user = userList.get(position);

        bidPrice.setText("Arttırdığı Fiyat: "+user.getPrice()+" TL");
        userName.setText(user.getName()+ " "+user.getSurname());

        Glide.with(context)
                .asBitmap()
                .load(user.getPhotoUrl())
                .into(profilePic);



        return rowView;
    }

}