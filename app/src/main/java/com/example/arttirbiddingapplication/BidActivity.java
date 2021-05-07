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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.Date;

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
    String tempPrice;
    private ArrayList<BidderList> bidderLists=new ArrayList<>();


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



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



        //fiyatı çek  , days of auction , yüklendiği tarih

        //PRODUCTS
        DocumentReference docRef = firebaseFirestore.collection("PRODUCTS").document(productId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                String daysofAuction = documentSnapshot.getString("daysOfAuction");
                Date date = documentSnapshot.getDate("date");




             //   name.setText(documentSnapshot.getString("name")
                // surname.setText(documentSnapshot.getString("surname"));
            }
        });

        //AUCTIONS
        DocumentReference docReff = firebaseFirestore.collection("Auctions").document(productId);
        docReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {



                String priceee = documentSnapshot.getString("startingPrice");
                tempPrice = price.getText().toString();
                String currentPrice = documentSnapshot.getString("currentprice");

                String bidders = documentSnapshot.getString("bidders");

                if(!(bidders.equals(" "))){

                }else{
                    Toast toast= DynamicToast.makeError(getApplicationContext(),"Kimse Daha Fiyat Arttırmadı!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }


                if(currentPrice.equals(" ")){
                        price.setText(priceee);
                        tempPrice = price.getText().toString();
                }else{
                        price.setText(currentPrice);
                        tempPrice = price.getText().toString();
                    }
            }
        });




        DocumentReference docRefasd = firebaseFirestore.collection("PRODUCTS").document(productId);
        docRefasd.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


              //  Date date = documentSnapshot.getDate("date");




                //   name.setText(documentSnapshot.getString("name")
                // surname.setText(documentSnapshot.getString("surname"));
            }
        });




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