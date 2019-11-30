package com.example.smartbiciunal;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class EnteredBikeParkActivity extends PositionAndMessageActivity implements OnMapReadyCallback {

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
    protected String getMarkerMessage() {
        return "Usted parqueó su bicicleta en este parquadero.";
    }

    @Override
    protected String getLocation() {
        // TODO change appropriately
        return "B";
    }

    @Override
    protected LatLng getLocationLatLng() {
        // TODO change appropriately
        return new LatLng(4.632829, -74.085173);
    }
}
