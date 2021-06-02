package com.example.arttirbiddingapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arttirbiddingapplication.Activities.BidActivity;
import com.example.arttirbiddingapplication.Adapters.RecyclerItemViewAdapter2;
import com.example.arttirbiddingapplication.Adapters.RecyclerLastChatAdapter;
import com.example.arttirbiddingapplication.Adapters.RecyclerMessagesAdapter;
import com.example.arttirbiddingapplication.Models.Chat;
import com.example.arttirbiddingapplication.Models.User;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MessageFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerLastChatAdapter recyclerLastChatAdapter;
    ArrayList<User> users=new ArrayList<>();
    ArrayList<String> chattedUser=new ArrayList<>();

    private FirebaseAuth fAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser fUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView=v.findViewById(R.id.lastchatRecyclerView);

        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();


        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getChattedUsers();

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                getUsers();
            }
        }, 1500);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                initRecyclerView();
                progressDialog.dismiss();
            }
        }, 2500);


        return v;
    }
    private void reinitRecyclerView() {

        recyclerLastChatAdapter.notifyDataSetChanged();


    }
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerLastChatAdapter = new RecyclerLastChatAdapter(getContext(), users);
        recyclerView.setAdapter(recyclerLastChatAdapter);
    }
    private void getChattedUsers(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chat");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);

                    if (fUser.getUid().equals(chat.getSenderId()) && !chattedUser.contains(chat.getReceiverId()) )
                    {
                        chattedUser.add(chat.getReceiverId());
                    }
                    if (fUser.getUid().equals(chat.getReceiverId()) && !chattedUser.contains(chat.getSenderId()))
                    {
                        chattedUser.add(chat.getSenderId());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }
    private void getUsers()
    {
        for (String s:chattedUser)
        {
            DocumentReference docRef = firebaseFirestore.collection("USERS").document(s);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    users.add(new User(s,documentSnapshot.getString("name"),documentSnapshot.getString("surname"),documentSnapshot.getString("photoUrl")));

                }
            });
        }

    }


}