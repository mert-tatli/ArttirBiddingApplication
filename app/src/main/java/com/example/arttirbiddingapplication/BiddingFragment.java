package com.example.arttirbiddingapplication;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.ArrayList;

public class BiddingFragment extends Fragment {

    private RecyclerView recyclerView,recyclerViewFinished;
    private RecyclerBiddingItemAdapter adapter;
    private RecyclerBiddingFinishedItemAdapter finishedItemAdapter;
    private ArrayList<String> defaultUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_bidding, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBidding);
        recyclerViewFinished=view.findViewById(R.id.recyclerViewFinished);
        defaultUri=new ArrayList<>();
        defaultUri.add("https://thumbor.forbes.com/thumbor/trim/0x360:4501x2892/fit-in/711x399/smart/https://specials-images.forbesimg.com/imageserve/5c0a960ca7ea43705919981f/0x0.jpg");
        defaultUri.add("https://cdn.motor1.com/images/mgl/YozWJ/s3/2020-gr-supra-2-0l-turbo.jpg");

       initOnGoingRecyclerView();
        initFinishedRecyclerView();
        return view;
    }


    private void initOnGoingRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerBiddingItemAdapter(getActivity(), defaultUri);
        recyclerView.setAdapter(adapter);
    }

    private void initFinishedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFinished.setLayoutManager(layoutManager);
        finishedItemAdapter = new RecyclerBiddingFinishedItemAdapter(getActivity(), defaultUri);
        recyclerViewFinished.setAdapter(finishedItemAdapter);
    }
}