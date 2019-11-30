package com.example.smartbiciunal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LeftCampusActivity extends AppCompatActivity {

    Button doneButton;
    TextView leftCampusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_campus);

        doneButton = findViewById(R.id.confirmHavingLeftCampusButton);
        leftCampusTextView = findViewById(R.id.leftCampusMessage);

        configureTextView();
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

    private void configureTextView() {
        // TODO
    }
}