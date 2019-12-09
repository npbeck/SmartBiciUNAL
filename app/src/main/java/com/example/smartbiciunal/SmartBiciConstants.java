package com.example.smartbiciunal;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SmartBiciConstants {
    public final static String LOCATION_NOT_ON_CAMPUS = "locations/NOT_ON_CAMPUS";

    public static QuerySnapshot STATIC_BIKE_PARK_DATA;
    // this string stores the user ID, so that other activities can use it to query the database for info
    static String userIdInDatabase = null;
    // this string stores the user's bike ID reference
    static String userBikeReferenceInDatabase = null;
    // this string stores the user's bike's location right before it left the bike park
    protected static String userBikeLocationBeforeItLeftTheBikePark = null;

    static void fetchStaticLocationsData(){
        FirebaseFirestore.getInstance().collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            STATIC_BIKE_PARK_DATA = task.getResult();
                    }
                });
    }

    protected static String getUserIdInDatabase(){
        return userIdInDatabase;
    }

    protected static String getUserBikeReferenceInDatabase(){
        //return userBikeReferenceInDatabase;
        // TODO change work-around
        return "bicycles/bike_1";
    }
}
