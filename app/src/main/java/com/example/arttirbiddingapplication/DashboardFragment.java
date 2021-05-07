package com.example.arttirbiddingapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
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

        categories.add(new Category("https://icons.iconarchive.com/icons/pelfusion/long-shadow-media/256/Mobile-Smartphone-icon.png","ELEKTRONİK"));
        categories.add(new Category("https://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Car-icon.png","ARABA"));
        categories.add(new Category("https://banner2.cleanpng.com/20180424/ibe/kisspng-basketball-sport-computer-icons-basketball-icon-5adf8feed8c600.9265103115246008148879.jpg","SPOR"));
        categories.add(new Category("https://image.flaticon.com/icons/png/512/568/568148.png","MOTOR"));
        categories.add(new Category("https://files.softicons.com/download/application-icons/circle-icons-by-martz90/png/512x512/go%20launcher.png","DİĞER"));


        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product p = document.toObject(Product.class);
                        products.add(p);
                    }
                    initRecyclerView();
                    initProductRecyclerView();
                } else {

                }
            }
        });
        return view;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        recyclerCategoryItemViewAdapter = new RecyclerCategoryItemViewAdapter(getActivity(), categories);
        categoryRecycler.setAdapter(recyclerCategoryItemViewAdapter);
    }


    private void initProductRecyclerView() {
        productRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerProductViewAdapter = new RecyclerProductViewAdapter(getActivity(), products);
        productRecycler.setAdapter(recyclerProductViewAdapter);
    }
}