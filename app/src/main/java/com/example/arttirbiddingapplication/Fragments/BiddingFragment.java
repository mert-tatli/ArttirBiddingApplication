package com.example.arttirbiddingapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.arttirbiddingapplication.Adapters.RecyclerBiddingFinishedItemAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerBiddingItemAdapter;
import com.example.arttirbiddingapplication.Models.Auction;
import com.example.arttirbiddingapplication.Models.Bidder;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BiddingFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewFinished;
    private RecyclerBiddingItemAdapter adapter;
    private RecyclerBiddingFinishedItemAdapter finishedItemAdapter;


    private ArrayList<Product> biddingItemsShow = new ArrayList<>(); //from products
    private ArrayList<Product> finishedBiddingItemsShow = new ArrayList<>();


    private ArrayList<String> biddingItems = new ArrayList<>();  //from auctions
    private ArrayList<String> finishedBiddingItems = new ArrayList<>();



    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bidding, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBidding);
        recyclerViewFinished = view.findViewById(R.id.recyclerViewFinished);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getFromAuctions();


        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {

            public void run() {
                getFromProductsOngoing();
                getFromProductsFinished();
            }
        }, 1500);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                initOnGoingRecyclerView();
                initFinishedRecyclerView();
                progressDialog.dismiss();
            }
        }, 2000);



        return view;
    }

    private void initOnGoingRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerBiddingItemAdapter(getActivity(), biddingItemsShow);
        recyclerView.setAdapter(adapter);
    }

    private void initFinishedRecyclerView() {
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFinished.setLayoutManager(layoutManager2);
        finishedItemAdapter = new RecyclerBiddingFinishedItemAdapter(getActivity(), finishedBiddingItemsShow);
        recyclerViewFinished.setAdapter(finishedItemAdapter);
    }

    public void getFromAuctions() {

        firebaseFirestore.collection("Auctions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Auction a = document.toObject(Auction.class);
                        if(checkDates(a.getDate()))
                        {
                            for (Bidder b : a.getBidders()) {
                                if (b.getBidderId().equals(fUser.getUid())) {
                                    finishedBiddingItems.add(document.getId());
                                    break;
                                }
                            }

                        }
                        else{
                            for (Bidder b : a.getBidders()) {
                                if (b.getBidderId().equals(fUser.getUid())) {
                                    biddingItems.add(document.getId());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void getFromProductsOngoing() {
        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (biddingItems.contains(document.getId())) {
                            Product p = document.toObject(Product.class);
                            biddingItemsShow.add(p);
                        }
                    }
                }
            }
        });
    }

    public void getFromProductsFinished() {

        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if (finishedBiddingItems.contains(document.getId())) {
                            Product p = document.toObject(Product.class);
                            finishedBiddingItemsShow.add(p);
                        }
                    }
                }
            }
        });
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
        if (!current_date.after(event_date)) {
            return false;
        }
        return true;
    }


}