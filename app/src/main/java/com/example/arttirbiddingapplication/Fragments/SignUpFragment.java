package com.example.arttirbiddingapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }
    private TextView alreadyHaveAccount;
    private FrameLayout parentFramelayout;

    private EditText email;
    private EditText name;
    private EditText surname;
    private EditText password;
    private EditText verification;
    private Button signup;
    private TextView errorMessage;
    private ProgressBar progressBar;

    private FirebaseAuth fAuth;
    private FirebaseFirestore firebaseFirestore;


    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);alreadyHaveAccount=view.findViewById(R.id.txt_already_have_account);
        parentFramelayout=getActivity().findViewById(R.id.register_framelayout);
        email=view.findViewById(R.id.txt_Email);
        name=view.findViewById(R.id.txt_Name);
        surname=view.findViewById(R.id.txt_Surname);
        password=view.findViewById(R.id.txt_password);
        verification=view.findViewById(R.id.txt_VerifyPassword);
        signup=view.findViewById(R.id.btn_SignUp);
        progressBar=view.findViewById(R.id.signupProgressBar);
        errorMessage=view.findViewById(R.id.errorText);

        fAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


         return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 verifyAuth();
            }
        });


    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFramelayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(name.getText())){
                if(!TextUtils.isEmpty(surname.getText())){
                    if(!TextUtils.isEmpty(password.getText()) && password.length()>=6){
                        if(!TextUtils.isEmpty(verification.getText()) && verification.length()>=6){
                            if(password.getText().toString().equals(verification.getText().toString())){
                                   errorMessage.setText("");
                                    signup.setEnabled(true);
                            }
                            else{
                                errorMessage.setText("Two passwords are not the same");
                                errorMessage.setTextColor(Color.RED);
                                signup.setEnabled(false);
                            }

                        }
                        else {
                            errorMessage.setText("Enter a verification password with more than 6 characters");
                            errorMessage.setTextColor(Color.RED);
                            signup.setEnabled(false);
                        }

                    }
                    else {
                        errorMessage.setText("Enter a password with more than 6 characters");
                        errorMessage.setTextColor(Color.RED);
                        signup.setEnabled(false);
                    }
                }
                else {
                    errorMessage.setText("Enter your surname");
                    errorMessage.setTextColor(Color.RED);
                    signup.setEnabled(false);
                }


            }
            else {
                errorMessage.setText("Enter your name");
                errorMessage.setTextColor(Color.RED);
                signup.setEnabled(false);
            }
        }
        else {
            errorMessage.setText("Enter your email");
            errorMessage.setTextColor(Color.RED);
            signup.setEnabled(false);
        }


    }

    private void verifyAuth(){
        if (email.getText().toString().matches(emailPattern)){

            progressBar.setVisibility(View.VISIBLE);


            fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                Map<Object,String> userInformation=new HashMap<>();
                                userInformation.put("name",name.getText().toString());
                                userInformation.put("surname",surname.getText().toString());
                                userInformation.put("city","");
                                userInformation.put("photoUrl","");

                                Map<Object,String> userInformation2=new HashMap<>();
                                userInformation2.put("userId",fAuth.getCurrentUser().getUid());
                                userInformation2.put("name",name.getText().toString());
                                userInformation2.put("surname",surname.getText().toString());
                                userInformation2.put("profileImage","");

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.child("Users").child(fAuth.getCurrentUser().getUid()).setValue(userInformation2);

                                firebaseFirestore.collection("USERS").document(fAuth.getCurrentUser().getUid())
                                        .set(userInformation, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            setFragment(new SignInFragment());
                                        }
                                        else{
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String error=task.getException().getMessage();
                                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                String error=task.getException().getMessage();
                                Toast toast= DynamicToast.makeWarning(getContext(),error, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 40);
                                toast.show();
                            }
                        }
                    });
        }
        else {

            progressBar.setVisibility(View.INVISIBLE);
             email.setError("Invalid Email!");
        }
    }

}