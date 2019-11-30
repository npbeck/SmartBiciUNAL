package com.example.smartbiciunal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * since both entry activities are very similar, this class encapsulates a lot of common functionality
 */
public abstract class PositionAndMessageActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_and_message);

        mapView = findViewById(R.id.general_map_view);
        textView = findViewById(R.id.general_text_view);

        configureMapView(savedInstanceState);
        configureTextView();
    }

    protected void configureTextView(){
        textView.setText(new StringBuilder()
                .append(getMessageBeginning())
                .append(getLocation())
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
        // settings that are the same for all extending classes
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // center camera in location

        // center camera in center of campus and zoom in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.6386345, -74.0868585), 15));

        // set camera bounds, so that only UNAL campus is displayed
        googleMap.setLatLngBoundsForCameraTarget(
                new LatLngBounds(new LatLng( 4.631505, -74.094344),new LatLng(4.645764, -74.079373)));

        // add marker
        googleMap.addMarker(new MarkerOptions()
                .position(getLocationLatLng())
                .title(getMarkerMessage()));
    }

    protected abstract String getMarkerMessage();

    /**
     *
     * @return the location of the door the bike entered through
     */
    protected abstract LatLng getLocationLatLng();

    /**
     *
     * @return name of the entrance door the bike entered through
     */
    protected abstract String getLocation();

}
