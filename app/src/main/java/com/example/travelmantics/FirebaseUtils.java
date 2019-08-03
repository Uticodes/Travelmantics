package com.example.travelmantics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
/**
 * Created by Utibe Etim
 * 2st/Aug/2019
 */
public class FirebaseUtils {
    public static FirebaseDatabase mFirebaseDataBase;
    public static DatabaseReference mDataBaseReference;
    private static FirebaseUtils firebaseUtils;
    public static ArrayList<TravelDeal> mDeals;

    public FirebaseUtils(){}

    public static void openFbReference(String ref){
        if (firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            mFirebaseDataBase = FirebaseDatabase.getInstance();

        }
        mDeals = new ArrayList<TravelDeal>();
        mDataBaseReference = mFirebaseDataBase.getReference().child(ref);
    }
}
