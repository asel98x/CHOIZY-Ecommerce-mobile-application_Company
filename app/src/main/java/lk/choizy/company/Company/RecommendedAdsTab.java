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
 * Use the {@link RecommendedAdsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendedAdsTab extends Fragment implements RecommendedListAdapter.OnItemListener, AdViewReco_BSheet.onDataChange{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recommendedList;
    private FloatingActionButton AddRecommendAdFAB;
    private ArrayList<RecommendedAD> adList;
    private DatabaseReference database;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> result;
    private ProgressBar recommenedLV_PG;
    private CompanyHelper db_helper;

    public RecommendedAdsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecommendedAdsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendedAdsTab newInstance(String param1, String param2) {
        RecommendedAdsTab fragment = new RecommendedAdsTab();
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
        View view =inflater.inflate(R.layout.fragment_recommended_ads_tab, container, false);
        recommendedList = view.findViewById(R.id.recommenedListV);
        AddRecommendAdFAB = view.findViewById(R.id.AddRecommenedAd);
        recommenedLV_PG = view.findViewById(R.id.recommenedLV_PG);
        adList = new ArrayList<>();
        db_helper = new CompanyHelper(getContext());
        RecommendedListAdapter adapter = new RecommendedListAdapter();
        recommendedList.setAdapter(adapter);
        recommendedList.addItemDecoration(new RecommendedListAdapter.RecommendedDecoration());
        adapter.setListener(this);
        recommendedList.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance().getReference(RecommendedAD.class.getSimpleName());

        database.orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                recommenedLV_PG.setVisibility(View.INVISIBLE);
                adList.clear();
                for (DataSnapshot one:dataSnapshot.getChildren()) {
                    RecommendedAD ad = one.getValue(RecommendedAD.class);
                    ad.setAd_ID(one.getKey());
                    adList.add(ad);
                }
                adapter.setList(adList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        AddRecommendAdFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),RecommendedAdd.class);
//                startActivity(intent);
                RecommendedAddBS addBS = new RecommendedAddBS();
                addBS.show(getChildFragmentManager(),"Add Recommended");
            }
        });


//        result = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if(result.getResultCode() == Activity.RESULT_OK){
//                            Intent intent = result.getData();
//                            if(intent != null){
//                                InsertRecommendedAd(intent.getData());
//                            }
//                        }
//                    }
//                });


        return view;
    }
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

//    public void InsertRecommendedAd(Uri ImgUri){
//        if(ImgUri != null ){
//            StorageReference storage = FirebaseStorage.getInstance().getReference(RecommendedAD.class.getSimpleName()).child(System.currentTimeMillis()
//                    + "." + getFileExtension(ImgUri));
//
//            storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
//                            Toast.makeText(getContext(),"Inserted",Toast.LENGTH_SHORT).show();
//                            RecommendedAD ad = new RecommendedAD(task.getResult().toString(),getCompanyId());
//                            database.push().setValue(ad);
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull @NotNull Exception e) {
//
//                }
//            });
//        }
//
//
//    }

    @Override
    public void ItemClickListener(int position) {
        AdViewReco_BSheet bottomSheet = new AdViewReco_BSheet(adList.get(position));
        bottomSheet.show(getChildFragmentManager(),"Recommended Ad ");
        bottomSheet.setListener(this);
    }

    @Override
    public void onDeleteAd(RecommendedAD ad) {
        database.child(ad.getAd_ID()).removeValue();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ad.getUrl());
        storageReference.delete();
    }

    @Override
    public void onImageChange(RecommendedAD ad, Uri imgUri) {
        storageReference = FirebaseStorage.getInstance().getReference(RecommendedAD.class.getSimpleName()).child(System.currentTimeMillis()
                + "." + getFileExtension(imgUri));

        storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        FirebaseStorage.getInstance().getReferenceFromUrl(ad.getUrl()).delete();
                        ad.setUrl(task.getResult().toString());
                        database.child(ad.getAd_ID()).setValue(ad);
                    }
                });
            }
        });
    }

    @Override
    public void onTitleUpdate(RecommendedAD ad) {
        db_helper.updateRecommended(ad).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getCompanyId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }
}