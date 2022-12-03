package lk.choizy.company;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DB_Class {

    FirebaseDatabase db;
    public DatabaseReference myRef;
    static Context context;

    public static final String CASharePreference = "Choizy_SharedPreference";


    public DB_Class(Context context) {
        this.context = context;
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();

    }

    public static Context getContext() {
        return context;
    }
}
