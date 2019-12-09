package com.example.smartbiciunal;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EnteredCampusActivity extends PositionAndMessageActivity
        implements OnMapReadyCallback,
        OnCompleteListener<DocumentSnapshot>, EventListener<DocumentSnapshot> {


    @Override
    protected String getMessageBeginning() {
        return "Su bicicleta ingresó al campus de la Universidad Nacional de Colombia, sede Bogotá. " +
                "Se encuentra en la ";
    }

    @Override
    protected String getMessageEnd() {
        return ". Encuentre el parqueadero de bicicletas más cercano en el mapa abajo.";
    }

    @Override
    protected String getMarkerMessage() {
        return "Usted ingresó al campus en esta puerta.";
    }

    @Override
    protected MarkerOptions getBikeLocationMarker(LatLng bikeLocation) {
        return new MarkerOptions()
                .position(bikeLocation)
                .title(getMarkerMessage())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        // add additional markers for all parking spots
        List<MarkerOptions> bikeParkMarkers = getBikeParkMarkers();
        for (MarkerOptions mo: bikeParkMarkers)
            googleMap.addMarker(mo);
    }

    private List<MarkerOptions> getBikeParkMarkers() {
        // store markers in this list
        LinkedList<MarkerOptions> bikeParkMarkers = new LinkedList<>();

        // work through bike parks in SmartBiciConstants.STATIC_BIKE_PARK_DATA

        for (QueryDocumentSnapshot snapshot: SmartBiciConstants.STATIC_BIKE_PARK_DATA){

            if (snapshot.getId().startsWith("bike_park")){
                snapshot.getData();
                long freeSpaces = (long) snapshot.get("capacity") - (long) snapshot.get("usage") ;

                bikeParkMarkers.add(new MarkerOptions()
                        .position(new LatLng(
                                Objects.requireNonNull(snapshot.getGeoPoint("location")).getLatitude(),
                                Objects.requireNonNull(snapshot.getGeoPoint("location")).getLongitude()
                        ))
                        .title(snapshot.getString("name"))
                        .snippet("Espacios libres: " + freeSpaces)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        }

        return bikeParkMarkers;
    }

    @Override
    protected void refreshViews() {
        // perform query for bike info on DB and register this class as listener
        getDB().document(SmartBiciConstants.getUserBikeReferenceInDatabase())
                .get()
                .addOnCompleteListener(this);
    }

    /*
    this method is triggered once the user's bike data changes
    It opens the EnteredBikeParkActivity if needed
     */
    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            // check whether the bike moved to a bike park
            if (((DocumentReference) Objects.requireNonNull(documentSnapshot.get("location"))).getId().startsWith("bike_park")){
                // stop listening to changes
                listenerRegistration.remove();

                startActivity(new Intent(this, EnteredBikeParkActivity.class));
            }
        }

    }
}
