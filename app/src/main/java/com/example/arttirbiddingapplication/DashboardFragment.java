package com.example.arttirbiddingapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
     private RecyclerView categoryRecycler,productRecycler;
     private RecyclerCategoryItemViewAdapter recyclerCategoryItemViewAdapter;
     private RecyclerProductViewAdapter recyclerProductViewAdapter;
     private ArrayList<Category> categories=new ArrayList<>();
    private ArrayList<Product> products=new ArrayList<>();
    private ArrayList<String> product_images=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dashboard, container, false);



        categoryRecycler = view.findViewById(R.id.categoryRecyclerView);
        productRecycler = view.findViewById(R.id.productRecyclerView);

        categories.add(new Category("https://icons.iconarchive.com/icons/pelfusion/long-shadow-media/256/Mobile-Smartphone-icon.png","ELEKTRONİK"));
        categories.add(new Category("https://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Car-icon.png","ARABA"));
        categories.add(new Category("https://banner2.cleanpng.com/20180424/ibe/kisspng-basketball-sport-computer-icons-basketball-icon-5adf8feed8c600.9265103115246008148879.jpg","SPOR"));
        categories.add(new Category("https://image.flaticon.com/icons/png/512/568/568148.png","MOTOR"));
        categories.add(new Category("https://files.softicons.com/download/application-icons/circle-icons-by-martz90/png/512x512/go%20launcher.png","DİĞER"));

        product_images.add("https://cdn.vatanbilgisayar.com/Upload/PRODUCT/samsung/thumb/117722-anaa_large.jpg");
        products.add(new Product("123456798","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));
        products.add(new Product("1234567982","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));
        products.add(new Product("1234567983","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));
        products.add(new Product("1234567984","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));
        products.add(new Product("1234567985","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));
        products.add(new Product("1234567985","Elektronik","Samsung s20 plus 512gb Hafıza","12","45",true,"3","asdasdasdasasdasdasd",product_images,"6SOpfEZyd2bORvgOF8xZqcvDugt2"));

        initRecyclerView();

        initProductRecyclerView();
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