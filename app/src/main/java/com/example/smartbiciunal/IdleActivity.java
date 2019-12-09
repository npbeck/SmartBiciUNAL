package com.example.smartbiciunal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Map;
import java.util.Objects;

public class IdleActivity extends AppCompatActivity implements EventListener<DocumentSnapshot>{

    TextView idleTextView;
    ImageView logo;
    FirebaseFirestore db;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        idleTextView = findViewById(R.id.idleMessage);
        logo = findViewById(R.id.SmartBiciUNALLogo);
        logo.setImageResource(R.drawable.bike_logo);
        db = FirebaseFirestore.getInstance();

        // register listener that triggers EnteredCampusActivity
        DocumentReference bikeReference = db.document(SmartBiciConstants.getUserBikeReferenceInDatabase());
        listenerRegistration = bikeReference.addSnapshotListener(this);
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot,
    @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }

        if (snapshot != null && snapshot.exists()) {
            // check whether the bike moved to a place within the campus
            // we are comparing REFERENCES, not values!
            DocumentReference newLocation = (DocumentReference) snapshot.get("location");
            DocumentReference notOnCampus = db.document(SmartBiciConstants.LOCATION_NOT_ON_CAMPUS);

            if (newLocation != null && ! newLocation.equals(notOnCampus)){
                // stop listening to changes
                listenerRegistration.remove();

                startActivity(new Intent(this, EnteredCampusActivity.class));
            }
        }
    }
}
