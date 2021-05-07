package com.example.arttirbiddingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class BidActivity extends AppCompatActivity {

    private ArrayList<String> pictures=new ArrayList<>();
    private TextView txtTitle,txtDetails,txtcondition,rateInfo,bidderList;
    private EditText price;
    private Button plus,minus;
    private Boolean condition;
    private RecyclerView recyclerView;
    private RecyclerItemViewAdapter2 recyclerItemViewAdapter2;
    private String increasingRate;
    private ArrayList<BidderList> bidderLists=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        txtcondition=findViewById(R.id.txtCondition);
        txtDetails=findViewById(R.id.txtDetails);
        txtTitle=findViewById(R.id.txtTitleBid);
        recyclerView=findViewById(R.id.recyclerItemImages);
        plus=findViewById(R.id.btnPlus);
        minus=findViewById(R.id.btnMinus);
        rateInfo=findViewById(R.id.rateInfo);
        price=findViewById(R.id.txtShowPrice);
        bidderList=findViewById(R.id.bidderList);

        Intent intent=getIntent();
        String productId=intent.getStringExtra("productId");
        String title=intent.getStringExtra("title");
        String  details=intent.getStringExtra("details");
        increasingRate=intent.getStringExtra("increasingRate");
        condition=intent.getBooleanExtra("condition",false);
        pictures=intent.getStringArrayListExtra("photos");





        bidderLists.add(new BidderList("123456789","Ahmer meşe","1500","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-zsO-WB5sqXt2_4XGhgNqeecBwQ2dm2dTFcV4NBR0hBIK9nlKRuIz8HUwEo-eEteJBm4&usqp=CAU"));
        bidderLists.add(new BidderList("123456789","Naz güleç","3500","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzWSvwyAYazHgANb4lGGqj2LEMX39QGbHtRQ&usqp=CAU"));
        bidderLists.add(new BidderList("123456789","Naz güleç","3500","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzWSvwyAYazHgANb4lGGqj2LEMX39QGbHtRQ&usqp=CAU"));



        rateInfo.setText("Bu ürünü en az "+increasingRate+"TL arttırabilirsiniz!");
        txtTitle.setText(title);
        txtDetails.setText(details);
       if (condition){
            txtcondition.setText("Yeni");
        }
        else{
            txtcondition.setText("İkinci el");
        }

        initRecyclerView();

        String tempPrice=price.getText().toString();


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum=Integer.parseInt(price.getText().toString())  + Integer.parseInt(increasingRate);
                price.setText(String.valueOf(sum));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(price.getText().toString()) >Integer.parseInt(tempPrice))
                {
                    int substraction=Integer.parseInt(price.getText().toString())  - Integer.parseInt(increasingRate);
                    price.setText(String.valueOf(substraction));
                }
                else
                {
                    Toast toast= DynamicToast.makeError(getApplicationContext(),"Mevcut Fiyatın altına inemezsiniz!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }

            }
        });


        bidderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (bidderLists.size()>0)
                    {
                        final Dialog dialog = new Dialog(BidActivity.this);
                        dialog.setContentView(R.layout.dialog_list_view);
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
                        }
                        ListView listView = dialog.findViewById(R.id.lv_bidding_users);
                        ArrayAdapter arrayAdapter = new BidderListAdapter(BidActivity.this, R.layout.assignment_dialog_list_layout, bidderLists);
                        listView.setAdapter(arrayAdapter);
                        listView.setClickable(false);
                        dialog.show();
                    }
                    else {
                        Toast toast=DynamicToast.makeError(BidActivity.this,"Bu ürün henüz arttırılmadı!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }


                }

        });

    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(BidActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerItemViewAdapter2 = new RecyclerItemViewAdapter2(BidActivity.this, pictures);
        recyclerView.setAdapter(recyclerItemViewAdapter2);
    }
    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(this,"up-to-bottom");
    }






}