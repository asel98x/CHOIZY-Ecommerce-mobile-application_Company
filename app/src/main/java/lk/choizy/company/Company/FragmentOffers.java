package lk.choizy.company.Company;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lk.choizy.company.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentOffers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOffers extends Fragment implements OfferListAdapter.OnItemClick, OfferViewBottomSheet.OnDeleteOffer {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton AddOffersFab;
    private List<Branch> branchList = new ArrayList<>();
    private Spinner branchDownDown;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private StorageReference storageReference;
    private ValueEventListener listener;
    private OfferBLAdapter adapter;
    private RecyclerView OfferLIstView;
    private OfferListAdapter RLadapter;
    private ArrayList<Offer> offerList;
    private ProgressBar OfferLV_PB;

    public FragmentOffers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOffers.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOffers newInstance(String param1, String param2) {
        FragmentOffers fragment = new FragmentOffers();
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
        View view =inflater.inflate(R.layout.fragment_offers, container, false);

        AddOffersFab = view.findViewById(R.id.AddOffersFab);
        branchDownDown = view.findViewById(R.id.BranchDropDown);
        OfferLIstView = view.findViewById(R.id.OfferLIstView);
        OfferLV_PB = view.findViewById(R.id.OfferLV_PB);


        adapter = new OfferBLAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,branchList);
        branchDownDown.setAdapter(adapter);
        RLadapter = new OfferListAdapter();
        RLadapter.setListener(this);



        OfferLIstView.setAdapter(RLadapter);
        OfferLIstView.addItemDecoration(new OfferListAdapter.OfferDecoration());
        OfferLIstView.setLayoutManager(new LinearLayoutManager(getContext()));


        myRef.child(Branch.class.getSimpleName()).orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for(DataSnapshot one:dataSnapshot.getChildren()){
                    Branch branch = one.getValue(Branch.class);
                    branch.setID(one.getKey());
                    adapter.add(branch);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });



        AddOffersFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OfferAdd.class);
                startActivity(intent);
            }
        });

        branchDownDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OfferLV_PB.setVisibility(View.VISIBLE);
                UpdateOfferList(branchList.get(i).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void UpdateOfferList(String Id){


        if(listener !=null){
            myRef.removeEventListener(listener);
        }

        offerList = new ArrayList<>();
        listener = myRef.child(Offer.class.getSimpleName()).orderByChild("branchID").equalTo(Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                OfferLV_PB.setVisibility(View.INVISIBLE);
                offerList.clear();
                for(DataSnapshot one:dataSnapshot.getChildren()){
                    Offer offer = one.getValue(Offer.class);
                    offer.setOfferId(one.getKey());
                    offerList.add(offer);
                }
                RLadapter.setList(offerList);
                RLadapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Snackbar.make(getView(),databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemClickLister(int position) {
        Offer offer = offerList.get(position);
        OfferViewBottomSheet bottomSheet = new OfferViewBottomSheet(offer);
        bottomSheet.show(getChildFragmentManager(),"Offer Bottom Sheet");
        bottomSheet.setListener(this);

    }
    @Override
    public void onDeleteBtnClick(Offer offer) {
        Query query=  myRef.child(Offer.class.getSimpleName()).orderByChild("offerUrl").equalTo(offer.getOfferUrl());

        myRef.child(Offer.class.getSimpleName()).child(offer.getOfferId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(offer.getOfferUrl());
                            storageReference.delete();
                        }
                        if(getView()!= null){
                            Snackbar.make(getView(),"Deleted",Snackbar.LENGTH_SHORT).show();
                        }

                        //TODO: delete kara back wala balanna

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        Snackbar.make(getView(),databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onUpdateBtnClick(Offer offer) {
        myRef.child(Offer.class.getSimpleName()).child(offer.getOfferId()).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(getView()!= null){
                    Snackbar.make(getView(),"Updated",Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onUploadImg(Uri uri,Offer offer) {
        String url = offer.getOfferUrl();
//        StorageReference storage = FirebaseStorage.getInstance("gs://choizy-academic.appspot.com").getReference(Offer.class.getSimpleName()).child(System.currentTimeMillis()
//                + "." + getFileExtension(uri));
//        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
//                        Query query=  myRef.child(Offer.class.getSimpleName());
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
//                                boolean using =false;
//                                for(DataSnapshot one:dataSnapshot.getChildren()){
//                                    Offer offer1 = one.getValue(Offer.class);
//                                    if(!one.getKey().equals(offer.getOfferId()) && offer1.getOfferUrl().equals(offer.getOfferUrl())){
//                                        using = true;
//
//                                    }
//                                }
//                                if(!using){
//                                    storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(offer.getOfferUrl());
//                                    storageReference.delete();
//                                }
//                                offer.setOfferUrl(task.getResult().toString());
//                                query.removeEventListener(this);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//
//                        myRef.child(Offer.class.getSimpleName()).child(offer.getOfferId()).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                });
//            }
//        });
        StorageReference storage = FirebaseStorage.getInstance().getReference(Offer.class.getSimpleName()).child(System.currentTimeMillis()
                + "." + getFileExtension(uri));
        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        offer.setOfferUrl(task.getResult().toString());
                        myRef.child(Offer.class.getSimpleName()).child(offer.getOfferId()).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                                Query query=  myRef.child(Offer.class.getSimpleName()).orderByChild("offerUrl").equalTo(url);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                        if(!dataSnapshot.exists()){

                                            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                                            storageReference.delete();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                        Snackbar.make(getView(),databaseError.getMessage(),Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(getView(),e.getMessage(),Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(requireActivity().getContentResolver().getType(uri));
    }

    private String getCompanyId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }

}