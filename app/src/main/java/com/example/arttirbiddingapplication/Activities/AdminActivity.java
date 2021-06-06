package com.example.arttirbiddingapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arttirbiddingapplication.Adapters.RecyclerAdminViewAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerProductViewAdapter;
import com.example.arttirbiddingapplication.Interfaces.OnDeleteItemListener;
import com.example.arttirbiddingapplication.Models.Auction;
import com.example.arttirbiddingapplication.Models.Bidder;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.Models.User;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firestore.v1.WriteResult;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AdminActivity extends AppCompatActivity implements OnDeleteItemListener {
    private RecyclerView productRecycler;
    private RecyclerAdminViewAdapter recyclerAdminViewAdapter;
    private ArrayList<Product> products=new ArrayList<>();
    private ArrayList<String> product_images=new ArrayList<>();
    private ArrayList<String> bidderIds=new ArrayList<>();
    private ArrayList<String> mails=new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private ImageView adminLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        productRecycler = findViewById(R.id.recyclerAdminItem);
        adminLogout=findViewById(R.id.adminLogout);

        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        initialize();



        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void initialize(){

        products.clear();

        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
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
                        products.add(p);


                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        public void run() {
                            initProductRecyclerView();
                            progressDialog.dismiss();
                        }
                    }, 500);
                }
            }
        });
    }

    private void reinitialize(){

        products.clear();

        firebaseFirestore.collection("PRODUCTS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product p = document.toObject(Product.class);
                        products.add(p);
                    }
                }
                initProductRecyclerView();
            }
        });
    }

    private void initProductRecyclerView() {
        productRecycler.setLayoutManager(new GridLayoutManager(AdminActivity.this,2));
        recyclerAdminViewAdapter = new RecyclerAdminViewAdapter(AdminActivity.this, products,this::OndeleteClick);
        productRecycler.setAdapter(recyclerAdminViewAdapter);
    }

    @Override
    public void OndeleteClick(String productId,String ownerId) {

        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getBidders(productId);
        getEmails(ownerId);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                deleteProduct(productId,ownerId);

            }
        }, 500);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {

            public void run() {
                sendMessage(mails);
                progressDialog.dismiss();
            }
        }, 1250);



    }

    public void getBidders(String productId){

        DocumentReference docRef = firebaseFirestore.collection("Auctions").document(productId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Auction a =documentSnapshot.toObject(Auction.class);
                if (!a.getBidders().isEmpty())
                {
                    for (Bidder b : a.getBidders()) {

                        if (!bidderIds.contains(b.getBidderId()))
                        {
                            bidderIds.add(b.getBidderId());
                        }
                    }
                }

            }
        });

    }

    public void sendMessage(ArrayList<String> mails){

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String username="arttirapplication@gmail.com";
        final String password="zahir757134mm";
        String message1="Ürününüz veya arttırdığınız ürün uygunsuz içerik veya benzeri sebeplerle yayından kaldırılmıştır!";
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session=Session.getInstance(properties,new  javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,password);
            }
        });
        try {
            InternetAddress[] i=new InternetAddress[12];
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            Address[] cc=new Address[mails.size()];
            for (int f=0;f<mails.size();f++)
            {
                cc[f]=new InternetAddress(mails.get(f));

            }
            message.addRecipients(Message.RecipientType.TO, cc);
            message.setSubject("Ürün bilgilendirmesi");
            message.setText(message1);
            Transport.send(message);

        }
        catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }

    }
    public void getEmails(String ownerId){
        firebaseFirestore.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if (bidderIds.contains(document.getId()) || document.getId().equals(ownerId))
                        {
                            User u = document.toObject(User.class);
                            mails.add(u.getEmail());
                        }

                    }
                }
            }
        });

    }
    public void deleteProduct(String productId,String sellerId)
    {
        firebaseFirestore.collection("PRODUCTS").document(productId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast= DynamicToast.makeError(AdminActivity.this,"Hata!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
                        toast.show();
                    }
                });


        firebaseFirestore.collection("Auctions").document(productId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toast= DynamicToast.makeSuccess(AdminActivity.this,"Ürün silindi ve kullanıcılar bilgilendirildi!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast= DynamicToast.makeError(AdminActivity.this,"Hata!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
                        toast.show();
                    }
                });



        reinitialize();

    }


}

