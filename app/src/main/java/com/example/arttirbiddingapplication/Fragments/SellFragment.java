package com.example.arttirbiddingapplication.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;

import com.example.arttirbiddingapplication.Adapters.RecyclerItemViewAdapter;
import com.example.arttirbiddingapplication.Activities.MainActivity;
import com.example.arttirbiddingapplication.Models.Auction;
import com.example.arttirbiddingapplication.Models.Bidder;
import com.example.arttirbiddingapplication.Models.Product;
import com.example.arttirbiddingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SellFragment extends Fragment {
    private static final int REQUEST_CODE_CHOOSE = 1;

    private FirebaseUser fUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<Uri> defaultUri = new ArrayList<>();
    private RecyclerView recyclerView;

    private String[] arraySpinner = new String[]{"Kategori Se??iniz", "Elektronik","Araba","Spor","Motor","Di??er"};
    private Spinner s1;
    private String category = "";
    private ImageView addImage;
    private EditText title, startprice, description,increasingRate;
    private Button savebtn;
    private CheckBox notusedCheck;
    private int daysOfAuction = 1;
    private boolean isImageChosen=false;
    private Uri imageUri;
    private RecyclerItemViewAdapter adapter;
    private String productId;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);

        title = view.findViewById(R.id.txt_Title);
        startprice = view.findViewById(R.id.txt_startingPrice);
        description = view.findViewById(R.id.txt_description);
        savebtn = view.findViewById(R.id.savebuttonsell);
        addImage = view.findViewById(R.id.addImage);
        notusedCheck = view.findViewById(R.id.notUsedCheck);
        increasingRate=view.findViewById(R.id.txt_increasingRate);
        s1 = view.findViewById(R.id.categorySpinner);
        SegmentedButtonGroup segmentedButtonGroup = (SegmentedButtonGroup) view.findViewById(R.id.buttonGroup_lordOfTheRings);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        getImages();

        segmentedButtonGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0) {
                    daysOfAuction = 1;
                } else if (position == 1) {
                    daysOfAuction = 3;
                } else if (position == 2) {
                    daysOfAuction = 5;
                }
            }
        });

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

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Foto??raf Se??iniz"), REQUEST_CODE_CHOOSE);

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

    private void checkInput() {
        if (!category.equals("Kategori Se??iniz")) {
            if (!TextUtils.isEmpty(title.getText())) {
                if (!TextUtils.isEmpty(startprice.getText())) {
                    if (!TextUtils.isEmpty(increasingRate.getText())) {
                        if (!TextUtils.isEmpty(description.getText())) {
                            if (isImageChosen==true && defaultUri.size()>=3 && defaultUri.size()<6) {

                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setTitle("??r??n haz??rlan??yor...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                uploadImages();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        if (!mImageUrls.contains(null))
                                        {
                                            Date currentdate = new Date();
                                            String endDate=addDate(currentdate,daysOfAuction);
                                            Product newproduct=new Product(productId,category,title.getText().toString(),startprice.getText().toString(),notusedCheck.isChecked(),description.getText().toString(),mImageUrls,fUser.getUid(),endDate);

                                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                                    .set(newproduct, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    ArrayList<Bidder> bidders=new ArrayList<>();
                                                    Auction auction=new Auction("",startprice.getText().toString(),increasingRate.getText().toString(),String.valueOf(daysOfAuction),endDate,bidders);


                                                    firebaseFirestore.collection("Auctions").document(productId)
                                                            .set(auction, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            progressDialog.dismiss();
                                                            Toast toast=DynamicToast.makeSuccess(getContext(),"Ba??ar??l??!", Toast.LENGTH_SHORT);
                                                            toast.setGravity(Gravity.TOP, 0, 40);
                                                            toast.show();
                                                            Intent intent=new Intent(getContext(),MainActivity.class);
                                                            startActivity(intent);
                                                            getActivity().finish();

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                        else{
                                            Toast toast=DynamicToast.makeError(getContext(),"Resimleri y??klerken bir sorun olu??tu. L??tfen tekrar deneyiniz!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP, 0, 40);
                                            toast.show();
                                        }

                                    }
                                }, 5500);

                            }
                            else {

                                Toast toast= DynamicToast.makeError(getContext(),"L??tfen 3 ile 5 adet aras??nda ??r??n resmi se??iniz !", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 40);
                                toast.show();
                            }

                        } else {
                            description.setError("A????klama k??sm??n?? bo?? b??rakmay??n !");
                        }

                    } else {
                        increasingRate.setError("Art???? Miktar??n?? Bo?? B??rakmay??n !");
                    }
                }
                else{
                    startprice.setError("Ba??lang???? Fiyat??n?? Bo?? B??rakmay??n !");
                }
            }
            else {
                title.setError("Ba??l??k K??sm??n?? Bo?? B??rakmay??n!");
            }
        }
        else {

            TextView error = (TextView) s1.getSelectedView();
            error.setError("Kategori Se??iniz !");

        }

    }

    private void getImages() {

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.addpic)
                + '/' + getResources().getResourceTypeName(R.drawable.addpic) + '/' + getResources().getResourceEntryName(R.drawable.addpic));
        defaultUri.add(imageUri);
        defaultUri.add(imageUri);
        defaultUri.add(imageUri);
        defaultUri.add(imageUri);

        initRecyclerView();

    }

    private void reinitRecyclerView() {

        adapter.notifyDataSetChanged();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerItemViewAdapter(getActivity(), defaultUri);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == MainActivity.RESULT_OK) {
                defaultUri.clear();
                if (data.getClipData() != null) {
                    isImageChosen=true;
                    int count = data.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        defaultUri.add(imageUri);
                        currentItem = currentItem + 1;
                    }

                } else if (data.getData() != null) {
                    isImageChosen=true;
                    Uri imagePath = data.getData();
                    defaultUri.add(imagePath);

                }
                reinitRecyclerView();
            }
        }
    }

    private void uploadImages() {
        String id=UUID.randomUUID().toString();
        for (int i = 0; i < defaultUri.size(); i++) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(fUser.getUid()).child("Product"+id).child(UUID.randomUUID().toString());
            productId="Product"+UUID.randomUUID();
            storageReference
                    .putFile(defaultUri.get(i)).addOnSuccessListener(taskSnapshot ->
                    storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                        String url = Objects.requireNonNull(task.getResult()).toString();
                        mImageUrls.add(url);

                    }))
                    .addOnFailureListener(e -> {

                    });
        }

    }

    public String addDate(Date date,int amount){

        Date dtStartDate=date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(dtStartDate);
        c.add(Calendar.DATE, amount);
        return sdf.format(c.getTime());

    }

}