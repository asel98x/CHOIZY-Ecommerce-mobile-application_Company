package lk.choizy.company.CompanyBraunch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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

import lk.choizy.company.Company.Offer;
import lk.choizy.company.Company.OfferListAdapter;
import lk.choizy.company.Company.OfferViewBottomSheet;
import lk.choizy.company.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Branch_Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Branch_Offers extends Fragment implements OfferListAdapter.OnItemClick,OfferViewBottomSheet.OnDeleteOffer{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView branchOfferListV;
    private FirebaseDatabase database ;
    private DatabaseReference myRef;
    private ArrayList<Offer> offerList;
    private StorageReference storageReference;
    private OfferListAdapter adapter;
    private ValueEventListener offerListener;
    private FloatingActionButton addBranchOffers;
    SearchView searchView;


    public Fragment_Branch_Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Branch_Offers.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Branch_Offers newInstance(String param1, String param2) {
        Fragment_Branch_Offers fragment = new Fragment_Branch_Offers();
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
        View view =inflater.inflate(R.layout.fragment__branch__offers, container, false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        branchOfferListV = view.findViewById(R.id.branchOfferListV);
        searchView = view.findViewById(R.id.search_BranchOffers);
        addBranchOffers = view.findViewById(R.id.addBranchOffers);
        offerList = new ArrayList<>();
        adapter = new OfferListAdapter();
        adapter.setListener(this);
        branchOfferListV.setAdapter(adapter);
        branchOfferListV.addItemDecoration(new OfferListAdapter.OfferDecoration());
        branchOfferListV.setLayoutManager(new LinearLayoutManager(getContext()));


        searchResult("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResult(newText);
                return true;
            }
        });

        addBranchOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),branchOfferAdd.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void searchResult(String searchTxt){

        if(offerListener != null){
            myRef.child(Offer.class.getSimpleName()).removeEventListener(offerListener);
        }

        offerListener = myRef.child(Offer.class.getSimpleName()).orderByChild("branchID").equalTo(getBranchId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                offerList.clear();
                for (DataSnapshot one:dataSnapshot.getChildren()) {
                    Offer offer = one.getValue(Offer.class);
                    offer.setOfferId(one.getKey());

                    if(!searchTxt.isEmpty()){
                        if(offer.getTitle().toLowerCase().contains(searchTxt.toLowerCase())||offer.getDescription().toLowerCase().contains(searchTxt.toLowerCase())||(""+offer.getPrice()).contains(searchTxt.toLowerCase())){
                            offerList.add(offer);
                        }

                    }else {
                        offerList.add(offer);
                    }
                }
                adapter.setList(offerList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    private String getBranchId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("BranchID",null);
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
                        Snackbar.make(getView(),"Deleted",Snackbar.LENGTH_SHORT).show();
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
                Snackbar.make(getView(),"Updated",Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onUploadImg(Uri uri, Offer offer) {
        String url = offer.getOfferUrl();
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
}