package lk.choizy.company.Company;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import lk.choizy.company.PasswordChangeDialog;
import lk.choizy.company.R;
import lk.choizy.company.SplashScreen;
import lk.choizy.company.Users;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCompanyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCompanyProfile extends Fragment implements PasswordChangeDialog.onPasswordChange,CompanyAbout_BS.AboutUpdate,Terms_conditions_BS.onTermsChange,Discount_Range_BS.onDiscountRangeChange{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ShapeableImageView profileImg;
    private MaterialButton updateProfileBtn,changePassBtn,aboutBtn,termsBtn,discountRangeBtn,logoutBtn;
    private TextView companyName,companyEmail,companyCategory;
    private Company company;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private CompanyHelper helper;

    public FragmentCompanyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCompanyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCompanyProfile newInstance(String param1, String param2) {
        FragmentCompanyProfile fragment = new FragmentCompanyProfile();
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
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);
        profileImg = view.findViewById(R.id.CompanyProfile_Img);
        companyName = view.findViewById(R.id.CompanyProfile_Name);
        companyEmail = view.findViewById(R.id.CompanyProfile_Email);
        companyCategory = view.findViewById(R.id.CompanyProfile_Category);
        updateProfileBtn = view.findViewById(R.id.CompanyProfile_UpdateBtn);
        changePassBtn = view.findViewById(R.id.CompanyProfile_PasswordBtn);
        aboutBtn = view.findViewById(R.id.CompanyProfile_AboutBtn);
        termsBtn = view.findViewById(R.id.CompanyProfile_TermsBtn);
        logoutBtn = view.findViewById(R.id.CompanyProfile_logoutBtn);
        discountRangeBtn = view.findViewById(R.id.CompanyProfile_DiscountBtn);

        helper = new CompanyHelper(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child(Company.class.getSimpleName()).child(getCompanyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    return;
                }
                company = dataSnapshot.getValue(Company.class);
                company.setKey(getCompanyId());
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompanyProfileUpdate_BS bottomSheet = new CompanyProfileUpdate_BS(company);
                bottomSheet.show(getChildFragmentManager(),"Company_Profile_UpdateSheet");
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordChangeDialog passChange = new PasswordChangeDialog();
                passChange.show(getChildFragmentManager(),"Password_ChangeCompany");
                passChange.setListener(FragmentCompanyProfile.this);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompanyAbout_BS aboutBottomSheet = new CompanyAbout_BS(company);
                aboutBottomSheet.setListener(FragmentCompanyProfile.this);
                aboutBottomSheet.show(getChildFragmentManager(),"About BottomSheet");
            }
        });

        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Terms_conditions_BS termsBottomSheet = new Terms_conditions_BS(company);
                termsBottomSheet.setListener(FragmentCompanyProfile.this);
                termsBottomSheet.show(getChildFragmentManager(),"BottomSheetTerms");
            }
        });

        discountRangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Discount_Range_BS discount_range = new Discount_Range_BS(company);
                discount_range.setListener(FragmentCompanyProfile.this);
                discount_range.show(getChildFragmentManager(),"Discount Range Sheet");
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.logoutCompPreference();
                Intent intent = new Intent(getContext(), SplashScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }

    private void updateUi(){
        System.out.println(company.getImageURL());
        Picasso.get().load(company.getImageURL()).fit().centerCrop().into(profileImg);
        companyName.setText(company.getCompany_name());
        companyEmail.setText(company.getCompany_email());
        companyCategory.setText(company.getCompany_category());

    }

    private String getCompanyId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }

    @Override
    public void NewPassword(String pass) {
        company.setCompany_password(pass);
        Users users = new Users(company.getCompany_email(),pass,Company.class.getSimpleName());
        myRef.child(Users.class.getSimpleName()).child(company.getKey()).setValue(users);
        myRef.child(Company.class.getSimpleName()).child(company.getKey()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Password Updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAboutUpdate(Company company) {
        myRef.child(Company.class.getSimpleName()).child(company.getKey()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"About Information Updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onTermsUpdated(Company company) {
        myRef.child(Company.class.getSimpleName()).child(company.getKey()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Terms and condition Updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDiscountRangeUpdate(Company company) {
        myRef.child(Company.class.getSimpleName()).child(company.getKey()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar.make(getView(),"Discount Range Updated",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}