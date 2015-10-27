package com.chaemil.gmapsapiexamples.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chaemil.gmapsapiexamples.R;

/**
 * Created by chaemil on 23.10.15.
 */
public class StartActivity extends AppCompatActivity {

    private Button mapButton;
    private View streetViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        getUI();
        setupUI();
    }

    private void getUI() {
        mapButton = (Button) findViewById(R.id.map_button);
        streetViewButton = findViewById(R.id.street_view_button);
    }

    private void setupUI() {
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(StartActivity.this, MapActivity.class);
                startActivity(map);
            }
        });

        streetViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent streetView = new Intent(StartActivity.this, StreetViewActivity.class);
                startActivity(streetView);
            }
        });
    }
}
