package lk.choizy.company.CompanyBraunch;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Company.Branch;
import lk.choizy.company.Company.Company;
import lk.choizy.company.PasswordChangeDialog;
import lk.choizy.company.R;
import lk.choizy.company.SplashScreen;
import lk.choizy.company.Users;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Branch_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Branch_Profile extends Fragment implements PasswordChangeDialog.onPasswordChange{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView branchName;
    private TextView branchEmail;
    private TextView branchMobile;
    BranchHelper helper;
    private Branch branch;
    private MaterialButton profileUpdateBtn,profileChangePasswordBtn,qrBtn,feedbackBtn,logoutBtn;
    private ValueEventListener profileListener;
    private FirebaseDatabase database;
    DatabaseReference myRef;

    public Fragment_Branch_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Branch_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Branch_Profile newInstance(String param1, String param2) {
        Fragment_Branch_Profile fragment = new Fragment_Branch_Profile();
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
        View view = inflater.inflate(R.layout.fragment__branch__profile, container, false);
        branchName = view.findViewById(R.id.profile_branchName);
        branchEmail = view.findViewById(R.id.profile_branchEmail);
        branchMobile = view.findViewById(R.id.profile_branchMobile);
        profileUpdateBtn = view.findViewById(R.id.profile_updateBtn);
        logoutBtn = view.findViewById(R.id.profile_logoutBtn);
        profileChangePasswordBtn = view.findViewById(R.id.profile_changePassBtn);
        qrBtn = view.findViewById(R.id.profile_QRBtn);
        feedbackBtn = view.findViewById(R.id.profile_FeedBack);
        logoutBtn = view.findViewById(R.id.profile_logoutBtn);
        helper = new BranchHelper(getContext());

         database = FirebaseDatabase.getInstance();

         feedbackBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getContext(),BranchFeedBack.class);
                 startActivity(intent);
             }
         });

        profileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate_BS profileBottomSheet = new profileUpdate_BS(branch);
                profileBottomSheet.show(getChildFragmentManager(),"profileUpdate bottomSheet");
            }
        });

        profileChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog();
                passwordChangeDialog.show(getChildFragmentManager(),"PasswordDialog");
                passwordChangeDialog.setListener(Fragment_Branch_Profile.this);
            }
        });

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRGen_BS qrGen_bs = new QRGen_BS();
                qrGen_bs.show(getChildFragmentManager(),"QR GEN BS");
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.logoutPreference();
                Intent intent = new Intent(getContext(), SplashScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });



        return view;
    }

    public void updateUi(){
        branchName.setText(branch.getName());
        branchEmail.setText(branch.getEmail());
        branchMobile.setText(branch.getMobile());
    }


    private String getBranchId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("BranchID",null);
    }


    @Override
    public void NewPassword(String pass) {
        Users users = new Users(branch.getEmail(),pass,Branch.class.getSimpleName());
        branch.setPassword(pass);
        myRef.child(Users.class.getSimpleName()).child(branch.getID()).setValue(users);
        myRef.child(Branch.class.getSimpleName()).child(branch.getID()).setValue(branch).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Password was updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        myRef = database.getReference();
        profileListener = myRef.child(Branch.class.getSimpleName()).child(getBranchId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                branch = dataSnapshot.getValue(Branch.class);
                branch.setID(dataSnapshot.getKey());
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        myRef.removeEventListener(profileListener);
    }
}