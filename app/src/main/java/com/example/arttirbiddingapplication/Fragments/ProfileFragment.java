package com.example.arttirbiddingapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arttirbiddingapplication.Activities.ChatActivity;
import com.example.arttirbiddingapplication.Models.Profile;
import com.example.arttirbiddingapplication.R;
import com.example.arttirbiddingapplication.Activities.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private String[] arraySpinner = new String[]{"Şehir Seçiniz", "Adana", "Adiyaman", "Afyon", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa", "Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir", "Gaziantep", "Giresun", "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul", "Izmir", "Kahramanmaras",
            "Karabuk", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Mardin", "Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt", "Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"};

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String from="";
    private ImageView imageView,imageView2;
    private Button save;
    private Spinner s1;
    private ImageView logOut;
    private EditText name,surname;
    private String photoUrl="";


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        name=view.findViewById(R.id.txt_Name2);
        surname=view.findViewById(R.id.txt_Surname2);
        imageView = view.findViewById(R.id.circleImageView);
        imageView2=view.findViewById(R.id.imageView2);
        save=view.findViewById(R.id.btn_updateProfile);
        s1 = view.findViewById(R.id.spinner);


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                from = arraySpinner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Yükleniyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        DocumentReference docRef = firebaseFirestore.collection("USERS").document(fUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name.setText(documentSnapshot.getString("name"));
                surname.setText(documentSnapshot.getString("surname"));

                if (documentSnapshot.getString("city") != null) {
                    int spinnerPosition = adapter.getPosition(documentSnapshot.getString("city"));
                    s1.setSelection(spinnerPosition);
                }

            }
        });

        DocumentReference docRef2 = firebaseFirestore.collection("USERS").document(fUser.getUid());
        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

              String uri= documentSnapshot.getString("photoUrl");
                if(!uri.equals(""))
                {
                    Picasso.get().load(uri).into(imageView);
                    progressDialog.dismiss();
                }
                else {
                    Picasso.get().load(R.drawable.profile_image).into(imageView);
                    progressDialog.dismiss();
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

        logOut = view.findViewById(R.id.btn_logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fAuth.getInstance().signOut();
                fAuth=null;
                Intent register = new Intent(getActivity(), RegisterActivity.class);
                startActivity(register);
                getActivity().finish();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        return view;
    }
    private void updateData(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kaydediliyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {

                if (!photoUrl.equals(""))
                {
                    Map<Object,String> userInformation=new HashMap<>();
                    userInformation.put("name",name.getText().toString());
                    userInformation.put("surname",surname.getText().toString());
                    userInformation.put("city",from);
                    userInformation.put("photoUrl",photoUrl);

                    Map<Object,String> userInformation2=new HashMap<>();
                    userInformation2.put("userId",fAuth.getCurrentUser().getUid());
                    userInformation2.put("name",name.getText().toString());
                    userInformation2.put("surname",surname.getText().toString());
                    userInformation2.put("profileImage",photoUrl);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Users").child(fAuth.getCurrentUser().getUid()).setValue(userInformation2);


                    firebaseFirestore.collection("USERS").document(fAuth.getCurrentUser().getUid())
                            .set(userInformation, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast toast=DynamicToast.makeSuccess(getContext(),"Başarılı!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 40);
                            toast.show();

                        }
                    });
                }
                else {
                    Toast toast=DynamicToast.makeError(getContext(),"Resimleri yüklerken bir sorun oluştu. Lütfen tekrar deneyiniz!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                    progressDialog.dismiss();
                }
            }
        }, 6000);

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(fUser.getUid()).child("profilePicture").child("profile.jpg");
            storageReference
                    .putFile(filePath).addOnSuccessListener(taskSnapshot ->
                    storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                        String url = Objects.requireNonNull(task.getResult()).toString();
                        photoUrl=url;
                    }))
                    .addOnFailureListener(e -> {

                    });
        }
    }

    private void checkInputs(){

            if(!TextUtils.isEmpty(name.getText())){
                if(!TextUtils.isEmpty(surname.getText())) {
                    if(!from.equals("Şehir Seçiniz")) {

                            uploadImage();
                            updateData();
                    }
                    else {
                        Toast toast=DynamicToast.makeWarning(getContext(),"Şehir Seçiniz!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 40);
                        toast.show();
                    }

                }
                else {
                    surname.setError("Soyisim Kısmını Boş Bırakmayın!");
                }

            }
            else {
                name.setError("İsim Kısmını Boş Bırakmayın!");
            }
    }

}