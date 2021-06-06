package com.example.arttirbiddingapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arttirbiddingapplication.Adapters.TabLayoutAdapter;
import com.example.arttirbiddingapplication.R;
import com.google.android.material.tabs.TabLayout;


public class MyAuctionsFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ViewPager viewPager=view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Arttırdıklarım"));
        tabLayout.addTab(tabLayout.newTab().setText("Ürünlerim"));


        TabLayoutAdapter adapter=new TabLayoutAdapter(getActivity(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_my_auctions, container, false);


        return view;
    }
}