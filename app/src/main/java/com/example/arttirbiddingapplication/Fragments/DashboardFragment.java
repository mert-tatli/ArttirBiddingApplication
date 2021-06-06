package com.example.arttirbiddingapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arttirbiddingapplication.Adapters.RecyclerCategoryItemViewAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerProductViewAdapter;
import com.example.arttirbiddingapplication.Models.Category;
import com.example.arttirbiddingapplication.Interfaces.OnCategoryItemListener;
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

public class DashboardFragment extends Fragment implements OnCategoryItemListener {
     private RecyclerView categoryRecycler,productRecycler;
     private RecyclerCategoryItemViewAdapter recyclerCategoryItemViewAdapter;
     private RecyclerProductViewAdapter recyclerProductViewAdapter;
     private ArrayList<Category> categories=new ArrayList<>();
    private ArrayList<Product> products=new ArrayList<>();
    private ArrayList<String> product_images=new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);

        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        categoryRecycler = view.findViewById(R.id.categoryRecyclerView);
        productRecycler = view.findViewById(R.id.productRecyclerView);


        categories.add(new Category("https://cdn0.iconfinder.com/data/icons/infographic-orchid-vol-1/256/Colorful_Label-512.png","Hepsi"));
        categories.add(new Category("https://icons.iconarchive.com/icons/pelfusion/long-shadow-media/256/Mobile-Smartphone-icon.png","Elektronik"));
        categories.add(new Category("https://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Car-icon.png","Araba"));
        categories.add(new Category("https://banner2.cleanpng.com/20180424/ibe/kisspng-basketball-sport-computer-icons-basketball-icon-5adf8feed8c600.9265103115246008148879.jpg","Spor"));
        categories.add(new Category("https://image.flaticon.com/icons/png/512/568/568148.png","Motor"));
        categories.add(new Category("https://files.softicons.com/download/application-icons/circle-icons-by-martz90/png/512x512/go%20launcher.png","Diğer"));


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product p = document.toObject(Product.class);
                        if (checkDates(p.getEndDate()))
                        {
                                products.add(p);
                        }
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        public void run() {
                            initRecyclerView();
                            initProductRecyclerView();
                            progressDialog.dismiss();
                        }
                    }, 500);


                }
            }
        });
        return view;
    }

    private void reinitRecyclerView(){
        recyclerProductViewAdapter.notifyDataSetChanged();
        productRecycler.setAdapter(recyclerProductViewAdapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        recyclerCategoryItemViewAdapter = new RecyclerCategoryItemViewAdapter(getActivity(), categories,this::OnCategoryClick);
        categoryRecycler.setAdapter(recyclerCategoryItemViewAdapter);
    }

    private void initProductRecyclerView() {
        productRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerProductViewAdapter = new RecyclerProductViewAdapter(getActivity(), products);
        productRecycler.setAdapter(recyclerProductViewAdapter);
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
            return false;
        }
        return true;
    }

    @Override
    public void OnCategoryClick(String data) {

        if (!data.equals("Hepsi"))
        {
            products.clear();
            firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product p = document.toObject(Product.class);

                            if (checkDates(p.getEndDate()))
                            {
                                if (p.getCategory().equals(data))
                                {
                                    products.add(p);
                                }

                            }
                        }
                        reinitRecyclerView();
                    }
                }
            });
        }
        else
        {
            products.clear();
            firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product p = document.toObject(Product.class);

                            if (checkDates(p.getEndDate()))
                            {
                                    products.add(p);
                            }
                        }
                        reinitRecyclerView();
                    }
                }
            });
        }

    }
}