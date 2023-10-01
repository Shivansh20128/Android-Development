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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LiftvsStair extends AppCompatActivity {
    Button start;
    Button stop;
    TextView status;
    TextView values;
    Sensor accelerometerSensor;
    double prevMotion=0;
    int initial=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lift_vs_stair);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        status = findViewById(R.id.status);
        values = findViewById(R.id.values);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sensor on","Accelerometer sensor turned on");
                if (accelerometerSensor == null) {
                    Toast.makeText(v.getContext(), "No accelerometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(v.getContext(), "Detection has started!", Toast.LENGTH_SHORT).show();
                    // registering our sensor with sensor manager.
                    sensorManager.registerListener(accelerometerSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(accelerometerSensorEventListener,accelerometerSensor);
                status.setText("");
                Toast.makeText(v.getContext(),"Detection turned off",Toast.LENGTH_SHORT).show();
                values.setText("");
            }
        });


    }

    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                float accelY = event.values[1];
                float accelZ = event.values[2];

                double motion = Math.sqrt((accelY * accelY) + (accelZ * accelZ));
                double motionChange = motion - prevMotion;
                prevMotion = motion;
                initial++;
                if(initial>1){
                    if(motionChange>3 && (event.values[2]>11 || event.values[2]<8) && (event.values[1]>0.5 || event.values[1]<-0.5)){
                        status.setText("You are walking on stairs");
                    }else if(motionChange>0.1 && motionChange<2 && (event.values[1]<0.4 && event.values[1]>-0.4) && (event.values[2]>10.2 ||  event.values[2]<9.4)){
                        status.setText("You are in a lift");
                    }
                }


                status.addTextChangedListener(
                        new TextWatcher() {
                            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                            final Handler handler = new Handler(Looper.getMainLooper());
                            Runnable workRunnable;
                            @Override public void afterTextChanged(Editable s) {
                                handler.removeCallbacks(workRunnable);
                                workRunnable = this::doSmth;
                                handler.postDelayed(workRunnable, 1000 );
                            }

                            private void doSmth() {
                                if(motionChange<=1){
                                    status.setText("You are not in lift, of lift is moving at constant velocity");
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
