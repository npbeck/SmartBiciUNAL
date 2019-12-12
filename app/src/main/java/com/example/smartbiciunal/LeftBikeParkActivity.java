package com.example.smartbiciunal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class LeftBikeParkActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    TextView leftBikeParkTextView;
    TextView onButtonClickTextView;
    Button okayButton;
    Button robberyButton;
    ProgressBar progressBar;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_bike_park);

        leftBikeParkTextView = findViewById(R.id.leftBikeParkMessage);
        okayButton = findViewById(R.id.confirmRemovalOfBikeButton);
        robberyButton = findViewById(R.id.robberyButton);
        onButtonClickTextView = findViewById(R.id.left_bike_park_on_button_click_message);
        progressBar = findViewById(R.id.loading_left_bike_park);

        configureOkayButton();
        configureRobberyButton();
        configureTextView();

        // register a listener that triggers the LeftCampusActivity
        DocumentReference bikeReference = FirebaseFirestore.getInstance().document(SmartBiciConstants.getUserBikePathInDatabase());
        listenerRegistration = bikeReference.addSnapshotListener(this);
    }

    private void configureTextView() {
        // tell user from which bike park his bike left
        String location = SmartBiciConstants.userBikeLocationBeforeItLeftTheBikePark;
        if (location == null)
            location = "XX";

        String newText = getString(R.string.preTextLeftBikePark) + " " +
                location +
                getString(R.string.postTextLeftBikePark);

        leftBikeParkTextView.setText(newText);

    }

    private void configureRobberyButton() {
        robberyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                DocumentReference bikeRef = FirebaseFirestore.getInstance()
                        .document(SmartBiciConstants.getUserBikePathInDatabase());

                bikeRef.update("stolen", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // actually update the view
                                robberyButton.setVisibility(View.INVISIBLE);
                                okayButton.setVisibility(View.INVISIBLE);
                                onButtonClickTextView.setText(R.string.investigating_robbery_message);
                                onButtonClickTextView.setBackgroundColor(Color.RED);
                                onButtonClickTextView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }

    private void configureOkayButton() {
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robberyButton.setVisibility(View.INVISIBLE);
                okayButton.setVisibility(View.INVISIBLE);
                onButtonClickTextView.setText(R.string.bike_left_park_no_problem);
                onButtonClickTextView.setBackgroundColor(Color.GREEN);
                onButtonClickTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
    this method is triggered once the bike data changes
     */
    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            // check whether the bike moved to a bike park
            if (((DocumentReference) Objects.requireNonNull(documentSnapshot.get("location"))).getId().startsWith("entrance")){
                // stop listening to changes
                listenerRegistration.remove();

                startActivity(new Intent(this, LeftCampusActivity.class));
            }
        }
    }
}
