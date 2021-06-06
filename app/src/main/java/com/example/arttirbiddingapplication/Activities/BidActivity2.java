package com.example.arttirbiddingapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arttirbiddingapplication.Adapters.BidderListAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerItemViewAdapter2;
import com.example.arttirbiddingapplication.Models.Auction;
import com.example.arttirbiddingapplication.Models.Bidder;
import com.example.arttirbiddingapplication.Models.Profile;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import maes.tech.intentanim.CustomIntent;

public class BidActivity2 extends AppCompatActivity {

    private ArrayList<String> pictures=new ArrayList<>();
    private TextView txtTitle,txtDetails,txtcondition,bidderList,txtTime,txtStartingPrice;
    private EditText price;
    private Boolean condition;
    private String tempPrice,date,title2,details2,sellerId;
    private String landingPrice="";
    private Button contact;
    private ArrayList<Bidder> bidders=new ArrayList<>();
    private ArrayList<Profile> bidInfo=new ArrayList<>();
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    private Handler handler = new Handler();
    private Runnable runnable;

    private RecyclerView recyclerView;
    private RecyclerItemViewAdapter2 recyclerItemViewAdapter2;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid2);

        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtcondition=findViewById(R.id.txtCondition1);
        txtDetails=findViewById(R.id.txtDetails1);
        txtTitle=findViewById(R.id.txtTitleBid1);
        recyclerView=findViewById(R.id.recyclerItemImages1);
        price=findViewById(R.id.txtShowPrice1);
        bidderList=findViewById(R.id.bidderList1);
        txtTime=findViewById(R.id.txtTime1);
        txtStartingPrice=findViewById(R.id.startingPriceText1);
        contact=findViewById(R.id.btnMessage2);

        Intent intent=getIntent();
        String productId=intent.getStringExtra("productId");
        String title=intent.getStringExtra("title");
        String  details=intent.getStringExtra("details");

        condition=intent.getBooleanExtra("condition",false);
        pictures=intent.getStringArrayListExtra("photos");
        tempPrice=intent.getStringExtra("startingPrice");
        sellerId=intent.getStringExtra("sellerId");

        if(sellerId.equals(fUser.getUid()))
        {
            contact.setVisibility(View.INVISIBLE);
            contact.setEnabled(false);
        }

        txtStartingPrice.setText(tempPrice+" TL");
        title2=title;
        details2=details;

        initialize(productId);
        initRecyclerView();
        checkCurrentBid(productId);

        bidderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bidders.isEmpty())
                {
                    final Dialog dialog = new Dialog(BidActivity2.this);
                    dialog.setContentView(R.layout.dialog_list_view);
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
                    }
                    ListView listView = dialog.findViewById(R.id.lv_bidding_users);
                    ArrayAdapter arrayAdapter = new BidderListAdapter(BidActivity2.this, R.layout.assignment_dialog_list_layout, bidInfo);
                    listView.setAdapter(arrayAdapter);
                    listView.setClickable(false);
                    dialog.show();
                }
                else {
                    Toast toast=DynamicToast.makeWarning(BidActivity2.this,"Bu ürün henüz arttırılmadı!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docRef = firebaseFirestore.collection("USERS").document(sellerId);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent intent1=new Intent(BidActivity2.this,ChatActivity.class);
                        intent1.putExtra("userId",sellerId);
                        intent1.putExtra("userName",documentSnapshot.getString("name")+" "+documentSnapshot.getString("surname"));
                        startActivity(intent1);
                        CustomIntent.customType(BidActivity2.this,"right-to-left");
                    }
                });

            }
        });

    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(BidActivity2.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerItemViewAdapter2 = new RecyclerItemViewAdapter2(BidActivity2.this, pictures);
        recyclerView.setAdapter(recyclerItemViewAdapter2);
    }
    @Override
    public void finish(){
        super.finish();
        CustomIntent.customType(this,"up-to-bottom");
    }

    public void getBidderList(){
        firebaseFirestore.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bidInfo.clear();

                    for ( int i=0;i<bidders.size();i++) {

                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            if (document.getId().equals(bidders.get(i).getBidderId()))
                            {
                                Profile p =document.toObject(Profile.class);
                                p.setPrice(bidders.get(i).getBidPrice());
                                bidInfo.add(p);
                            }
                        }

                    }
                    Collections.reverse(bidInfo);
                }
            }
        });

    }

    public void getasd(String productId){

        DocumentReference docRef = firebaseFirestore.collection("Auctions").document(productId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot !=null){
                        Auction a=documentSnapshot.toObject(Auction.class);

                        bidders=a.getBidders();
                        date=a.getDate();

                        landingPrice=a.getCurrentprice();

                        tempPrice = price.getText().toString();
                        if(a.getCurrentprice().isEmpty()){
                            price.setText(a.getStartingPrice());
                            tempPrice = price.getText().toString();
                        }else{

                            price.setText(a.getCurrentprice());
                            tempPrice = price.getText().toString();
                        }


                    }
                }
            }
        });
    }

    private void countDownStart() {

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(date);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        txtTime.setText(String.format("%02d", Days)+"g "+ String.format("%02d", Hours)+"s "+ String.format("%02d", Minutes)+"dk "+String.format("%02d", Seconds)+"sn");
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private boolean checkDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date event_date = null;
        try {
            event_date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date current_date = new Date();
        if (!current_date.after(event_date))
        {
            return false;
        }
        return true;
    }

    private void checkCurrentBid(String productId){

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);

                    DocumentReference docRef = firebaseFirestore.collection("Auctions").document(productId);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot !=null){
                                    String currentPrice=documentSnapshot.getString("currentprice");
                                    if (!currentPrice.isEmpty())
                                    {
                                        if (!landingPrice.equals(currentPrice))
                                        {
                                            price.setText(currentPrice);
                                            landingPrice=currentPrice;
                                            tempPrice=currentPrice;
                                            initialize(productId);
                                            Toast toast=DynamicToast.makeWarning(BidActivity2.this,"Fiyat Değişti!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP, 0, 40);
                                            toast.show();
                                        }
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void initialize(String productId)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getasd(productId);
        countDownStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                if (checkDates())
                {
                    txtTime.setText("Bu ürünün listelenme süresi bitti!");

                }
                getBidderList();
                progressDialog.dismiss();
            }
        }, 1000);


        txtTitle.setText(title2);
        txtDetails.setText(details2);
        if (condition){
            txtcondition.setText("Yeni");
        }
        else{
            txtcondition.setText("İkinci el");
        }
    }
}