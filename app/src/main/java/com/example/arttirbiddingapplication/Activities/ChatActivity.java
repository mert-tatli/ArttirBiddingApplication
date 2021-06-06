package com.example.arttirbiddingapplication.Activities;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arttirbiddingapplication.Adapters.RecyclerMessagesAdapter;
import com.example.arttirbiddingapplication.Fragments.MessageFragment;
import com.example.arttirbiddingapplication.Models.Chat;
import com.example.arttirbiddingapplication.Models.User;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private DatabaseReference reference;

    private ArrayList<Chat> chatList = new ArrayList<>();
    private ImageView imgBack;
    private RecyclerView chatRecyclerView;
    private ImageButton btnSendMessage;
    private EditText etMessage;
    private TextView tvUserName;
    private ImageView imgProfile;
    private Boolean checkClick = false;
    private static String senderPhoto, recieverPhoto;

    private Handler handler = new Handler();
    private Runnable runnable;

    private RecyclerMessagesAdapter recyclerMessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        imgBack = findViewById(R.id.imgBack);
        etMessage = findViewById(R.id.etMessage);
        tvUserName = findViewById(R.id.tvUserName);
        imgProfile = findViewById(R.id.imgProfile);

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");
        tvUserName.setText(userName);

        reference = FirebaseDatabase.getInstance().getReference();

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getReceiverPhoto(fUser.getUid());
        getSenderPhoto(userId);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                readMessage(fUser.getUid(), userId);
                progressDialog.dismiss();
            }
        }, 1000);

        checkMessages(userId);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();

                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "message is empty", Toast.LENGTH_SHORT).show();
                    etMessage.setText("");
                } else {
                    sendMessage(fUser.getUid(), userId, message);
                    etMessage.setText("");
                }
                checkClick = true;
                readMessage(fUser.getUid(), userId);
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(ChatActivity.this,MainActivity.class);
                intent1.putExtra("message","as");
                startActivity(intent1);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendMessage(String senderId, String receiverId, String message) {

        HashMap hashMap = new HashMap<String, String>();
        hashMap.put("senderId", senderId);
        hashMap.put("receiverId", receiverId);
        hashMap.put("message", message);

        reference.child("Chat").push().setValue(hashMap);

    }

    private void readMessage(String senderId, String receiverId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = chatList.size();
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getSenderId().equals(senderId) && chat.getReceiverId().equals(receiverId) || chat.getSenderId().equals(receiverId) && chat.getReceiverId().equals(senderId)) {
                        chatList.add(chat);
                    }
                }
                recyclerMessagesAdapter = new RecyclerMessagesAdapter(getApplicationContext(), chatList, senderPhoto, recieverPhoto);
                if (checkClick) {
                    recyclerMessagesAdapter.notifyDataSetChanged();
                }
                if (chatList.size() != size) {
                    chatRecyclerView.setAdapter(recyclerMessagesAdapter);
                    chatRecyclerView.scrollToPosition(chatList.size() - 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    private void checkMessages(String userId) {

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    readMessage(fUser.getUid(), userId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public String getReceiverPhoto(String receiverId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(receiverId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                recieverPhoto = user.getProfileImage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
        return null;
    }

    public String getSenderPhoto(String senderId) {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(senderId);
        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                senderPhoto = user.getProfileImage();

                if (senderPhoto.isEmpty()) {
                    imgProfile.setImageResource(R.drawable.profile_image);
                } else {
                    Glide.with(ChatActivity.this).load(senderPhoto).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        };
        databaseReference2.addListenerForSingleValueEvent(valueEventListener2);
        return "";

    }

}