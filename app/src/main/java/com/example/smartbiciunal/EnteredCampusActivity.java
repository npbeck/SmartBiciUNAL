package com.example.smartbiciunal;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class EnteredCampusActivity extends PositionAndMessageActivity implements OnMapReadyCallback {


    @Override
    protected String getMessageBeginning() {
        return "Su bicicleta ingresó al campus de la Universidad Nacional de Colombia, sede Bogotá. " +
                "Se encuentra en la puerta ";
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
    protected LatLng getLocationLatLng() {
        // TODO change appropriately
        return new LatLng(4.632829, -74.085173);
    }

    @Override
    protected String getLocation() {
        // TODO change appropriately
        return "26";
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
        // TODO change appropriately
        // add some dummy bike parks
        LinkedList<MarkerOptions> bikeParkMarkers = new LinkedList<>();
        bikeParkMarkers.add(new MarkerOptions()
                .position(new LatLng(4.631096, -74.082793))
                .title("parqueadero A")
                .snippet("Museo de Arquitectura")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        bikeParkMarkers.add(new MarkerOptions()
                .position(new LatLng(4.633692, -74.081627))
                .title("parqueadero B")
                .snippet("Facultad de Artes - Música")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        bikeParkMarkers.add(new MarkerOptions()
                .position(new LatLng(4.638186, -74.082346))
                .title("parqueadero C")
                .snippet("Edeficio Ciencias y Tecnologías")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        return bikeParkMarkers;
    }
}
