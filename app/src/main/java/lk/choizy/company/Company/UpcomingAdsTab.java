package lk.choizy.company.Company;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingAdsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingAdsTab extends Fragment implements AdsListAdapter.onItemSelected, AdViewUPC_BSheet.onADChange{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView upC_AdList;
    private FloatingActionButton addUpC_AdsBtn;
    private ArrayList<UpcomingAD> adList;
    private DatabaseReference database;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> result;
    private ProgressBar upcomingAdLV_PG;



    public UpcomingAdsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingAdsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingAdsTab newInstance(String param1, String param2) {
        UpcomingAdsTab fragment = new UpcomingAdsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_ads_tab, container, false);

        upC_AdList = view.findViewById(R.id.upcomingAdListV);
        addUpC_AdsBtn = view.findViewById(R.id.AddUpcomingAdds);
        upcomingAdLV_PG = view.findViewById(R.id.upcomingAdLV_PG);

        AdsListAdapter adapter = new AdsListAdapter();
        adList = new ArrayList<>();
        upC_AdList.setAdapter(adapter);
        upC_AdList.addItemDecoration(new AdsListAdapter.AdDecoration());
        adapter.setListener(this);
        upC_AdList.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance().getReference(UpcomingAD.class.getSimpleName());

        database.orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                upcomingAdLV_PG.setVisibility(View.INVISIBLE);
                adList.clear();
                for (DataSnapshot one:dataSnapshot.getChildren()) {
                    UpcomingAD ad = one.getValue(UpcomingAD.class);
                    ad.setId(one.getKey());
                    adList.add(ad);
                }
                adapter.setList(adList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        addUpC_AdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent img = new Intent();
                img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                result.launch(img);
//                Intent intent = new Intent(getActivity(),AdvertisementAdd.class);
//                startActivity(intent);
            }
        });

        result = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                InsertAd(intent.getData());

                            }
                        }
                    }
                });


        return view;
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    private void InsertAd(Uri ImgUri){
        String compID = getCompanyId();
        if (ImgUri != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference(UpcomingAD.class.getSimpleName()).child(System.currentTimeMillis()
                    + "." + getFileExtension(ImgUri));

            storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(getView() != null){
                                Snackbar.make(getView(),"Inserted",Snackbar.LENGTH_SHORT).show();
                            }

                            UpcomingAD Ad = new UpcomingAD(compID,task.getResult().toString());
                            database.push().setValue(Ad);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void ItemSelected(int position) {
        AdViewUPC_BSheet bottomSheet = new AdViewUPC_BSheet(adList.get(position));
        bottomSheet.show(getParentFragmentManager(),"Upcoming Ad ");
        bottomSheet.setListener(this);
    }

    @Override
    public void onDelete(UpcomingAD ad) {
        database.child(ad.getId()).removeValue();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ad.getUrl());
        storageReference.delete();
    }

    @Override
    public void onChangeImage(UpcomingAD ad, Uri ImgUri) {
        storageReference = FirebaseStorage.getInstance().getReference(UpcomingAD.class.getSimpleName()).child(System.currentTimeMillis()
                + "." + getFileExtension(ImgUri));

        storageReference.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        FirebaseStorage.getInstance().getReferenceFromUrl(ad.getUrl()).delete();
                        ad.setUrl(task.getResult().toString());
                        database.child(ad.getId()).setValue(ad);
                    }
                });
            }
        });
    }

    private String getCompanyId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }

}