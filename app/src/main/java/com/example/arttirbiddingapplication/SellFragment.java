package com.example.arttirbiddingapplication;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URL;
import java.net.URI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class SellFragment extends Fragment {

    private static final String TAG = "SellFragment";
    private EditText title,startprice,description;
    private Button savebtn;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ArrayList<String> mImageUrls=new ArrayList<>();
    private RecyclerView recyclerView;

    private String[] arraySpinner = new String[]{"Kategori Seçiniz","Elektronik","Ev Eşyaları","Oyun","Araç","Araç parçaları","Bahçe ve Hırdavat","Spor ve Outdoor","Moda ve Aksesuar","Bebek ve Çocuk","Film,Kitap ve Müzik","Diğer"};
    private Spinner s1;
    private String category="";
    private ImageView imageView;
    private RadioGroup radioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sell, container, false);

        fAuth=FirebaseAuth.getInstance();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView=view.findViewById(R.id.recyclerView);

        title=view.findViewById(R.id.txt_Title);
        startprice=view.findViewById(R.id.txt_startingPrice);
        description = view.findViewById(R.id.txt_description);
        savebtn=view.findViewById(R.id.savebuttonsell);





        getImages();


        s1 = view.findViewById(R.id.categorySpinner);


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                category = arraySpinner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });



        return view;
    }

    private void checkInput(){
       if(!category.equals("Kategori Seçiniz")){
         if(!TextUtils.isEmpty(title.getText())){
            if(!TextUtils.isEmpty(startprice.getText())){
                if(!TextUtils.isEmpty(description.getText())){


                }else{
                    description.setError("Açıklama kısmını boş bırakmayın !");
                }
            }else{
                startprice.setError("Başlangıç Fiyatını Boş Bırakmayın !");
            }
        }else{
            title.setError("Başlık Kısmını Boş Bırakmayın!");
        }
       }else{
          // Toast.makeText(getContext(),"Kategori Seçiniz!",Toast.LENGTH_LONG).show();
           TextView error = (TextView)s1.getSelectedView();
           error.setError("Kategori Seçiniz !");

       }

    }

    private void getImages(){


        StorageReference profileRef = storageReference.child("defaultIcon/camera.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                //URL url = Picasso.get().load(uri).toURL();



            }
        });



        mImageUrls.add("https://assets.adidas.com/images/w_600,f_auto,q_auto/e022471a90b54cfc86d9aa4300af3967_9366/Advantage_Ayakkabi_Beyaz_EE7677_01_standard.jpg");
        mImageUrls.add("https://assets.adidas.com/images/w_600,f_auto,q_auto/e022471a90b54cfc86d9aa4300af3967_9366/Advantage_Ayakkabi_Beyaz_EE7677_01_standard.jpg");
        mImageUrls.add("https://assets.adidas.com/images/w_600,f_auto,q_auto/e022471a90b54cfc86d9aa4300af3967_9366/Advantage_Ayakkabi_Beyaz_EE7677_01_standard.jpg");
        mImageUrls.add("https://assets.adidas.com/images/w_600,f_auto,q_auto/e022471a90b54cfc86d9aa4300af3967_9366/Advantage_Ayakkabi_Beyaz_EE7677_01_standard.jpg");
        initRecyclerView();

    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerItemViewAdaptor adapter = new RecyclerItemViewAdaptor(getActivity(), mImageUrls);
        recyclerView.setAdapter(adapter);
    }
}