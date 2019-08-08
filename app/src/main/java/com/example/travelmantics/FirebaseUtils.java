package com.example.travelmantics;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Utibe Etim
 * 2st/Aug/2019
 */
public class FirebaseUtils {
    public static FirebaseDatabase mFirebaseDataBase;
    public static DatabaseReference mDataBaseReference;
    private static FirebaseUtils firebaseUtils;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static ArrayList<TravelDeal> mDeals;
    private static final int RC_SIGN_IN = 234;
    private static ListActivity caller;
    public FirebaseUtils(){}
    public static boolean isAdmin;

    public static void openFbReference(String ref, final ListActivity callerActivity){
        if (firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            mFirebaseDataBase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        firebaseUtils.signIn();
                    }
                    else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome back", Toast.LENGTH_LONG).show();
                }
            };
            connectStorage();
        }
        mDeals = new ArrayList<TravelDeal>();
        mDataBaseReference = mFirebaseDataBase.getReference().child(ref);
    }


    // Configuring offline persistence
    public static FirebaseDatabase getDatabase() {
        if (mFirebaseDataBase == null) {
            mFirebaseDataBase = FirebaseDatabase.getInstance();
            mFirebaseDataBase.setPersistenceEnabled(true);
        }
        return mFirebaseDataBase;
    }

    public static void attachListener (){
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
    public static void detachListener (){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        //.setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }
    public static void signOut() {
        AuthUI.getInstance()
                .signOut(caller)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Logout", "User Logged Out");
                        Toast.makeText(caller, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUtils.attachListener();
                    }
                });
    }

    static void checkAdmin(String uid){
        FirebaseUtils.isAdmin = false;
        DatabaseReference ref = mFirebaseDataBase.getReference()
                .child("administrators")
                .child(uid);

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtils.isAdmin = true;
                Log.d("Admin", "You are an administrator");
                caller.showMenu();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }
    public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
    }


}
