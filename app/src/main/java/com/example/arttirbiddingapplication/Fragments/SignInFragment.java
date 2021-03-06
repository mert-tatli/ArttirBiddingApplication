package com.example.arttirbiddingapplication.Fragments;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arttirbiddingapplication.Activities.AdminActivity;
import com.example.arttirbiddingapplication.Activities.MainActivity;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


public class  SignInFragment extends Fragment {

    public SignInFragment() {

    }

    private TextView dontHaveAnAccount;
    private FrameLayout parentFramelayout;

    private ProgressBar progressBar;

    private EditText email;
    private EditText password;
    private Button singIn;
    private TextView errorMessageLogin;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ImageView gotoHome;


    private FirebaseAuth fAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);

        dontHaveAnAccount=view.findViewById(R.id.txt_dont_have_account);
        parentFramelayout=getActivity().findViewById(R.id.register_framelayout);

        email=view.findViewById(R.id.txt_Email2);
        password=view.findViewById(R.id.txt_password2);
        singIn=view.findViewById(R.id.btn_SignIn);
        errorMessageLogin=view.findViewById(R.id.errorMessageLogin);
        progressBar=view.findViewById(R.id.signInProgressBar);
        gotoHome=view.findViewById(R.id.gotohome);

        fAuth=FirebaseAuth.getInstance();

        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
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

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyInputs();
            }
        });


    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFramelayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {

            if (!TextUtils.isEmpty(password.getText())) {

                singIn.setEnabled(true);


            } else {
                password.setError("??ifrenizin 6 karakter veya daha fazla olmas?? gerekmektedir.");
            }
        } else {
            email.setError("Email giriniz");
        }


    }
    private void verifyInputs(){

        if (email.getText().toString().equals("admin")&& password.getText().toString().equals("admin"))
        {
            Intent intent=new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else {
            if (email.getText().toString().matches(emailPattern)){
                if (password.length()>=6){
                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else{
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
                    errorMessageLogin.setText("");
                    errorMessageLogin.setText("??ifrenizin 6 karakter veya daha fazla olmas?? gerekmektedir!");
                }
            }
            else {
                errorMessageLogin.setText("");
                errorMessageLogin.setText("L??tfen emailinizi veya ??ifrenizi do??ru girdi??inizden emin olun!");
            }
        }



    }



}