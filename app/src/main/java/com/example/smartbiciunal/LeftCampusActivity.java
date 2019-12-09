package com.example.smartbiciunal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.util.Objects.requireNonNull;

public class LeftCampusActivity extends AppCompatActivity implements OnSuccessListener<DocumentSnapshot> {

    Button doneButton;
    TextView leftCampusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_campus);

        doneButton = findViewById(R.id.confirmHavingLeftCampusButton);
        leftCampusTextView = findViewById(R.id.leftCampusMessage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(SmartBiciConstants.getUserBikeReferenceInDatabase())
                .get()
                .addOnSuccessListener(this);
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

    private void configureTextView(String location, boolean stolen) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (stolen){
            String text = getString(R.string.preTextLeftCampusRobbery)
                    + location
                    + getString(R.string.postTextLeftCampusRobbery);
            leftCampusTextView.setText(text);

            // mark bike as not stolen anymore
            db.document(SmartBiciConstants.getUserBikeReferenceInDatabase())
                    .update("stolen", false);
        }
        else{
            String text = getString(R.string.preTextLeftCampus)
                    + location
                    + getString(R.string.postTextLeftCampus);
            leftCampusTextView.setText(text);
        }

        // mark bike as having left the campus
        db.document(SmartBiciConstants.getUserBikeReferenceInDatabase())
                .update("location",  db.document(SmartBiciConstants.LOCATION_NOT_ON_CAMPUS));



    }

    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        // TODO change request, so it returns the name field
        String location = ((DocumentReference) requireNonNull(documentSnapshot
                .get("location"))).getId();
        boolean stolen = (boolean) documentSnapshot.get("stolen");

        configureTextView(location, stolen);
    }
}