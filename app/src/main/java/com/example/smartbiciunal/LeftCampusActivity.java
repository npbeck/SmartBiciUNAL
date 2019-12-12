package com.example.smartbiciunal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.util.Objects.requireNonNull;

public class LeftCampusActivity extends AppCompatActivity implements OnSuccessListener<DocumentSnapshot> {

    Button doneButton;
    TextView leftCampusTextView;

    private String location = null;
    private boolean stolen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_campus);

        doneButton = findViewById(R.id.confirmHavingLeftCampusButton);
        leftCampusTextView = findViewById(R.id.leftCampusMessage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(SmartBiciConstants.getUserBikePathInDatabase())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DocumentReference entranceRef = (DocumentReference) documentSnapshot.get("location");
                        stolen = (boolean) documentSnapshot.get("stolen");

                        FirebaseFirestore.getInstance()
                                .document(requireNonNull(entranceRef).getPath())
                                .get()
                                .addOnSuccessListener(LeftCampusActivity.this);
                    }
                });
        configureButton();
    }

    /*
    bring up the idle activity once the user acknowledged leaving campus
     */
    private void configureButton() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeftCampusActivity.this, IdleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void makeTextView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (stolen){
            String text = getString(R.string.preTextLeftCampusRobbery) + " "
                    + location
                    + getString(R.string.postTextLeftCampusRobbery);
            leftCampusTextView.setText(text);

            // mark bike as not stolen anymore
            db.document(SmartBiciConstants.getUserBikePathInDatabase())
                    .update("stolen", false);
        }
        else{
            String text = getString(R.string.preTextLeftCampus)
                    + location
                    + getString(R.string.postTextLeftCampus);
            leftCampusTextView.setText(text);
        }

        // mark bike as having left the campus
        db.document(SmartBiciConstants.getUserBikePathInDatabase())
                .update("location",  db.document(SmartBiciConstants.LOCATION_NOT_ON_CAMPUS));



    }

    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        location = documentSnapshot.getString("name");
        makeTextView();
    }
}