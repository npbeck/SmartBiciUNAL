package com.example.smartbiciunal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

/**
 * since both entry activities are very similar, this class encapsulates a lot of common functionality
 */
public abstract class PositionAndMessageActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        OnCompleteListener<DocumentSnapshot>, EventListener<DocumentSnapshot> {

    FirebaseFirestore db;

    MapView mapView;
    GoogleMap map;
    TextView textView;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_and_message);

        mapView = findViewById(R.id.general_map_view);
        textView = findViewById(R.id.general_text_view);

        configureMapView(savedInstanceState);
        configureTextView("<buscando ubicación>");

        // connect to database
        db = FirebaseFirestore.getInstance();

        // register listener for when bike enters a bike park
        registerActivityChangeListener();
    }

    protected void registerActivityChangeListener() {
        // register listener that triggers a new activity
        DocumentReference bikeReference = db.document(SmartBiciConstants.getUserBikeReferenceInDatabase());
        listenerRegistration = bikeReference.addSnapshotListener(this);
    }

    protected FirebaseFirestore getDB(){
        return db;
    }

    protected void configureTextView(String location){
        textView.setText(new StringBuilder()
                .append(getMessageBeginning())
                .append(location)
                .append(getMessageEnd()).toString());
    }

    protected abstract String getMessageBeginning();

    protected abstract String getMessageEnd();

    protected void configureMapView(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getString(R.string.google_maps_key));
        }

        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        refreshViews();
        // settings that are the same for all extending classes
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // center camera in center of campus and zoom in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.6386345, -74.0868585), 15));

        // set camera bounds, so that only UNAL campus is displayed
        googleMap.setLatLngBoundsForCameraTarget(
                new LatLngBounds(new LatLng( 4.631505, -74.094344),new LatLng(4.645764, -74.079373)));
    }



    protected abstract void refreshViews();

    protected abstract String getMarkerMessage();

    /*
   refresh all necessary views with the bike's information
   - entrance name
   - location
   - bike parks info
   */
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()){
            DocumentReference locationRef = (DocumentReference) Objects.requireNonNull(task.getResult()).get("location");

            // get bike's location data
            for (QueryDocumentSnapshot doc: SmartBiciConstants.STATIC_BIKE_PARK_DATA){
                DocumentReference currentDocumentReference = doc.getReference();
                if (currentDocumentReference.equals(locationRef)){
                    String entranceName = Objects.requireNonNull(doc.get("name")).toString();
                    LatLng bikeLocation =
                            new LatLng(Objects.requireNonNull(doc.getGeoPoint("location")).getLatitude(),
                                    Objects.requireNonNull(doc.getGeoPoint("location")).getLongitude());

                    // refresh text view with actual entrance name
                    configureTextView(entranceName);

                    // set entrance location on map
                    MarkerOptions bikeLocationMarker = getBikeLocationMarker(bikeLocation);
                    map.addMarker(bikeLocationMarker);
                }
            }
        }
    }

    protected abstract MarkerOptions getBikeLocationMarker(LatLng bikeLocation);

    @Override
    public abstract void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e);
}
