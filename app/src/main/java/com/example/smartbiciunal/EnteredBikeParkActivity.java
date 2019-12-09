package com.example.smartbiciunal;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

import static java.util.Objects.*;

public class EnteredBikeParkActivity extends PositionAndMessageActivity
        implements OnMapReadyCallback, OnCompleteListener<DocumentSnapshot> {

    @Override
    protected String getMessageBeginning() {
        return "Su bicicleta ingresó al parquadero ";
    }

    @Override
    protected String getMessageEnd() {
        return ". En el caso de que salga nuevamente del parquadero la vamos a notificar. " +
                "Le confirmamos la ubicación de su bicicleta en el mapa abajo.";
    }

    @Override
    protected void refreshViews() {
        // perform query for bike info on DB and register this class as listener
        getDB().document(SmartBiciConstants.getUserBikeReferenceInDatabase())
                .get()
                .addOnCompleteListener(this);
    }

    @Override
    protected String getMarkerMessage() {
        return "Usted parqueó su bicicleta en este parquadero.";
    }

    @Override
    protected MarkerOptions getBikeLocationMarker(LatLng bikeLocation) {
        return new MarkerOptions()
                .position(bikeLocation)
                .title(getMarkerMessage())
                .snippet("Fecha en que la parqueó: " + Calendar.getInstance().getTime().toString() + ".")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            // check whether the bike moved to a bike park
            if (((DocumentReference) requireNonNull(documentSnapshot.get("location"))).getId().equals("LEFT_BIKE_PARK")){
                // stop listening to changes
                listenerRegistration.remove();

                // TODO not quite working yet. Make it return the name field of the previous parking lot
                // set last bike park location for next Activity
                SmartBiciConstants.userBikeLocationBeforeItLeftTheBikePark = ((DocumentReference) requireNonNull(documentSnapshot
                        .get("location"))).getId();

                startActivity(new Intent(this, LeftBikeParkActivity.class));
            }
        }
    }
}
