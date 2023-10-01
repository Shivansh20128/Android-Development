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

public class LiftDetector extends AppCompatActivity {
    Button lift_detection_start;
    Button lift_detection_stop;
    TextView lift_status;
    TextView lift_values;
    Sensor accelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lift_activity);
        lift_detection_start = findViewById(R.id.lift_start);
        lift_detection_stop = findViewById(R.id.lift_stop);
        lift_status = findViewById(R.id.lift_status);
        lift_values = findViewById(R.id.lift_values);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lift_detection_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sensor on","Accelerometer sensor turned on");
                if (accelerometerSensor == null) {
                    Toast.makeText(v.getContext(), "No accelerometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // registering our sensor with sensor manager.v
                    Toast.makeText(v.getContext(), "Detection has started!", Toast.LENGTH_SHORT).show();

                    sensorManager.registerListener(accelerometerSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        lift_detection_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(accelerometerSensorEventListener,accelerometerSensor);
                lift_status.setText("");
                Toast.makeText(v.getContext(),"Detection turned off",Toast.LENGTH_SHORT).show();
                lift_values.setText("");
            }
        });
    }

    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

                if(event.values[2]<9.4){
                    lift_status.setText("Lift going down!\nOR\nYour up destination has arrived.");
                }else if(event.values[2]>10.2){
                    lift_status.setText("Lift going up!\nOR\nYour down destination has arrived.");
                }

                lift_status.addTextChangedListener(
                        new TextWatcher() {
                            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                            final Handler handler = new Handler(Looper.getMainLooper());
                            Runnable workRunnable;
                            @Override public void afterTextChanged(Editable s) {
                                handler.removeCallbacks(workRunnable);
                                workRunnable = this::doSmth;
                                handler.postDelayed(workRunnable, 750 );
                            }

                            private void doSmth() {
                                if(event.values[2]<=11 && event.values[2]>=9){
                                    lift_status.setText("You are not in lift, of lift is moving at constant velocity");
                                }
                            }
                        }
                );

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
