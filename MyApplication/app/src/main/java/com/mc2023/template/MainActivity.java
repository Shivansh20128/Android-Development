package com.mc2023.template;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView stairButton;
    TextView liftButton;
    TextView stepButton;
    TextView lift_stair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stairButton = findViewById(R.id.stairButton);
        liftButton = findViewById(R.id.liftButton);
        stepButton = findViewById(R.id.stepCounter);
        lift_stair = findViewById(R.id.lift_stair);

        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StrideLength.class);
                v.getContext().startActivity(intent);
            }
        });

        liftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LiftDetector.class);
                v.getContext().startActivity(intent);
            }
        });

        stairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StairDetector.class);
                v.getContext().startActivity(intent);
            }
        });

        lift_stair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LiftvsStair.class);
                v.getContext().startActivity(intent);
            }
        });
        

    }
}
