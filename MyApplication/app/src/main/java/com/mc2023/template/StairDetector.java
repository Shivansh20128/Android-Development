package com.mc2023.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StairDetector extends AppCompatActivity {
    Button stair_detection_start;
    Button stair_detection_stop;
    TextView stair_text;
    TextView stair_values;
    Sensor accelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stair_activity);
        stair_detection_start = findViewById(R.id.stair_start);
        stair_detection_stop = findViewById(R.id.stair_stop);
        stair_text = findViewById(R.id.stair_status);
        stair_values = findViewById(R.id.stair_values);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stair_detection_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sensor on","Accelerometer sensor turned on");
                if (accelerometerSensor == null) {
                    Toast.makeText(v.getContext(), "No accelerometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // registering our sensor with sensor manager.
                    Toast.makeText(v.getContext(), "Detection has started!", Toast.LENGTH_SHORT).show();
                    sensorManager.registerListener(accelerometerSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        stair_detection_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(accelerometerSensorEventListener,accelerometerSensor);
                stair_text.setText("");
                Toast.makeText(v.getContext(),"Detection turned off",Toast.LENGTH_SHORT).show();
                stair_values.setText("");

            }
        });


    }

    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

                if(event.values[1]>1 && event.values[2]<8){
                    stair_text.setText("You are on stairs!");
                }else if(event.values[1]>1 && event.values[2]>12){
                    stair_text.setText("You are on stairs!");
                }

                stair_text.addTextChangedListener(
                        new TextWatcher() {
                            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                            final Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
                            Runnable workRunnable;
                            @Override public void afterTextChanged(Editable s) {
                                handler.removeCallbacks(workRunnable);
                                workRunnable = this::doSmth;
                                handler.postDelayed(workRunnable, 1500 /*delay*/);
                            }

                            private void doSmth() {
                                if(event.values[2]<=11 && event.values[2]>=9){
                                    stair_text.setText("You are not moving on stairs");
                                }
                            }
                        }
                );



//                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
