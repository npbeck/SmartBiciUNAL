package com.example.smartbiciunal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class IdleActivity extends AppCompatActivity {

    TextView idleTextView;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        idleTextView = findViewById(R.id.idleMessage);
        logo = findViewById(R.id.SmartBiciUNALLogo);
    }
}
