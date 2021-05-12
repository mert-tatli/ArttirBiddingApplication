package com.example.arttirbiddingapplication;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class BidActivity extends AppCompatActivity {

    private ArrayList<String> pictures=new ArrayList<>();
    private TextView txtTitle,txtDetails,txtcondition,rateInfo,bidderList,txtTime;
    private EditText price;
    private Button plus,minus,btnBid;
    private Boolean condition;
    private String increasingRate,tempPrice,date;
    private ArrayList<Bidder> bidders=new ArrayList<>();
    private ArrayList<Profile> bidInfo=new ArrayList<>();
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;

    private RecyclerView recyclerView;
    private RecyclerItemViewAdapter2 recyclerItemViewAdapter2;

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
        btnBid=findViewById(R.id.btnBid);
        txtTime=findViewById(R.id.txtTime);

        Intent intent=getIntent();
        String productId=intent.getStringExtra("productId");
        String title=intent.getStringExtra("title");
        String  details=intent.getStringExtra("details");
        condition=intent.getBooleanExtra("condition",false);
        pictures=intent.getStringArrayListExtra("photos");
        tempPrice=intent.getStringExtra("startingPrice");



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.show();

        getasd(productId);
        getBidderList();
        countDownStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                if (checkDates())
                {
                    Toast toast=DynamicToast.makeError(BidActivity.this,"Bu ürünün Arttırma süresi doldu!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                    progressDialog.dismiss();
                    Intent intent1=new Intent(BidActivity.this,MainActivity.class);
                    startActivity(intent1);
                    finish();

                }
                progressDialog.dismiss();
            }
        }, 2500);

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
                    Toast toast= DynamicToast.makeWarning(getApplicationContext(),"Mevcut Fiyatın altına inemezsiniz!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                }

            }
        });


        bidderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bidders.isEmpty())
                    {
                        Collections.reverse(bidInfo);
                        final Dialog dialog = new Dialog(BidActivity.this);
                        dialog.setContentView(R.layout.dialog_list_view);
                        if (dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
                        }
                        ListView listView = dialog.findViewById(R.id.lv_bidding_users);
                        ArrayAdapter arrayAdapter = new BidderListAdapter(BidActivity.this, R.layout.assignment_dialog_list_layout, bidInfo);
                        listView.setAdapter(arrayAdapter);
                        listView.setClickable(false);
                        dialog.show();
                    }
                    else {
                        Toast toast=DynamicToast.makeWarning(BidActivity.this,"Bu ürün henüz arttırılmadı!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
                        toast.show();
                    }
                }
        });

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (fUser!=null)
                    {
                        if (!price.getText().toString().equals(tempPrice)) {


                            bidders.add(new Bidder(fUser.getUid(),price.getText().toString()));

                            Map<Object,ArrayList<Bidder>> userInformation=new HashMap<>();
                            userInformation.put("bidders",bidders);

                            firebaseFirestore.collection("Auctions").document(productId)
                                    .set(userInformation, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Map<Object,String> currentPrice=new HashMap<>();
                                    currentPrice.put("currentprice",price.getText().toString());
                                    firebaseFirestore.collection("Auctions").document(productId)
                                            .set(currentPrice, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast toast=DynamicToast.makeSuccess(BidActivity.this,"Başarılı!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP, 0, 40);
                                            toast.show();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    });

                                }
                            });
                        }
                        else {
                            Toast toast=DynamicToast.makeError(BidActivity.this,"Arttırabilmek için fiyatı değiştirmeniz gerekmektedir!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 40);
                            toast.show();
                        }
                    }
                    else
                    {
                        Toast toast=DynamicToast.makeError(BidActivity.this,"Arttırabilmek için giriş yapmanız gerekmektedir.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
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

    public void getBidderList(){
        firebaseFirestore.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        for (int i=0;i<bidders.size();i++)
                        {
                            if (bidders.get(i).getBidderId().equals(document.getId()))
                            {
                                Profile p =document.toObject(Profile.class);
                                p.setPrice(bidders.get(i).getBidPrice());
                                bidInfo.add(p);
                            }
                        }

                    }

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

                        tempPrice = price.getText().toString();
                        if(a.getCurrentprice().isEmpty()){
                            price.setText(a.getStartingPrice());
                            tempPrice = price.getText().toString();
                        }else{
                            price.setText(a.getCurrentprice());
                            tempPrice = price.getText().toString();
                        }
                        rateInfo.setText("Bu ürünü en az "+a.getIncreasingRate()+"TL arttırabilirsiniz!");
                        increasingRate=a.getIncreasingRate();

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
                       txtTime.setText(String.format("%02d", Days)+" Gün "+ String.format("%02d", Hours)+" Saat "+ String.format("%02d", Minutes)+" Dakika "+String.format("%02d", Seconds)+" Saniye");
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
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





}