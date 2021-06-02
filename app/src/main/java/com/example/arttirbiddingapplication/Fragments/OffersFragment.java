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

import com.example.arttirbiddingapplication.Adapters.RecyclerBiddingFinishedOffersAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerBiddingOffersAdapter;
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

public class OffersFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewFinished;
    private RecyclerBiddingOffersAdapter adapter;
    private RecyclerBiddingFinishedOffersAdapter finishedItemAdapter;


    private ArrayList<Product> biddingItemsShow = new ArrayList<>(); //from products
    private ArrayList<Product> finishedBiddingItemsShow = new ArrayList<>();


    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBiddingOffer);
        recyclerViewFinished = view.findViewById(R.id.recyclerViewFinishedOffer);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getFromProductsOngoing();

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {

            public void run() {
                getFromProductsFinished();
            }
        }, 1000);

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
        adapter = new RecyclerBiddingOffersAdapter(getActivity(), biddingItemsShow);
        recyclerView.setAdapter(adapter);
    }

    private void initFinishedRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFinished.setLayoutManager(layoutManager);
        finishedItemAdapter = new RecyclerBiddingFinishedOffersAdapter(getActivity(), finishedBiddingItemsShow);
        recyclerViewFinished.setAdapter(finishedItemAdapter);
    }

    public void getFromProductsOngoing() {
        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                            Product p = document.toObject(Product.class);
                            if (p.getSellerId().equals(fUser.getUid()) && !checkDates(p.getEndDate())){
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

                        Product p = document.toObject(Product.class);
                        if (p.getSellerId().equals(fUser.getUid()) && checkDates(p.getEndDate())){
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