package com.example.smartbiciunal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LeftBikeParkActivity extends AppCompatActivity {

    TextView leftBikeParkTextView;
    TextView onButtonClickTextView;
    Button okayButton;
    Button robberyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_bike_park);

        leftBikeParkTextView = findViewById(R.id.leftBikeParkMessage);
        okayButton = findViewById(R.id.confirmRemovalOfBikeButton);
        robberyButton = findViewById(R.id.robberyButton);
        onButtonClickTextView = findViewById(R.id.left_bike_park_on_button_click_message);

        configureOkayButton();
        configureRobberyButton();
        cofigureTextView();
    }

    private void cofigureTextView() {
        // TODO tell user which parking spot his bike is leaving from

    }

    private void configureRobberyButton() {
        robberyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robberyButton.setVisibility(View.INVISIBLE);
                okayButton.setVisibility(View.INVISIBLE);
                onButtonClickTextView.setText(R.string.investigating_robbery_message);
                onButtonClickTextView.setBackgroundColor(Color.RED);
                onButtonClickTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void configureOkayButton() {
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                robberyButton.setVisibility(View.INVISIBLE);
                okayButton.setVisibility(View.INVISIBLE);
                onButtonClickTextView.setText(R.string.bike_left_park_no_problem);
                onButtonClickTextView.setBackgroundColor(Color.GREEN);
                onButtonClickTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}
