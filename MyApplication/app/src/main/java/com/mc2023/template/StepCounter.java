package com.mc2023.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StepCounter extends AppCompatActivity {
    Button step_counting_start;
    Button direction_start;
    Button step_stop;
    Button show_trace;
    TextView direction;
    TextView steps;
    TextView dist;
    Sensor accelerometerSensor;
    Sensor magnetometerSensor;
    ArrayList<Integer> angle = new ArrayList<>();

    double old_acc = 0;
    Integer stepCount = -1;
    Integer threshold = 3;
    float strideLength= (float) 0.7;
    float distance=0;

    private final float[] a_y = new float[3];
    private final float[] mag = new float[3];
    private boolean cond_a_y = false;
    private boolean cond_mag = false;
    private static double rotationInDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_activity);

        Intent intent = getIntent();
        int stride = intent.getIntExtra("stride",70);
        step_counting_start = findViewById(R.id.step_start);
        direction_start = findViewById(R.id.direction_start);
        step_stop = findViewById(R.id.step_stop);
        steps = findViewById(R.id.step_status);
        direction = findViewById(R.id.direction_text);
        dist = findViewById(R.id.distance);
        show_trace = findViewById(R.id.show_trace);


        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        step_counting_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angle.clear();
                Log.d("sensor on","Accelerometer sensor turned on");
                if (accelerometerSensor == null) {
                    Toast.makeText(v.getContext(), "No accelerometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // registering our sensor with sensor manager.
                    sensorManager.registerListener(accelerometerSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        direction_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sensor on","Magnetometer sensor turned on");
                if (magnetometerSensor == null) {
                    Toast.makeText(v.getContext(), "No Magnetometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // registering our sensor with sensor manager.
                    sensorManager.registerListener(magnetometerSensorEventListener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }

                if (accelerometerSensor == null) {
                    Toast.makeText(v.getContext(), "No accelerometer sensor found in device.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // registering our sensor with sensor manager.
                    sensorManager.registerListener(magnetometerSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        step_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(accelerometerSensorEventListener,accelerometerSensor);
                sensorManager.unregisterListener(magnetometerSensorEventListener,accelerometerSensor);
                sensorManager.unregisterListener(magnetometerSensorEventListener,magnetometerSensor);
                steps.setText("");
                Toast.makeText(v.getContext(),"Detection turned off",Toast.LENGTH_SHORT).show();
                direction.setText("");
                dist.setText("");
                distance=0;
                stepCount=0;


            }
        });

        show_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MyCanvas.class);
                intent.putExtra("angles",angle);
                intent.putExtra("stride",stride);
                v.getContext().startActivity(intent);
            }
        });




    }

    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor sensorStep = sensorEvent.sensor;
            if(sensorStep != null){
                float x_a = sensorEvent.values[0];
                float y_a = sensorEvent.values[1];
                float z_a = sensorEvent.values[2];

                double a_magnitude = Math.sqrt((x_a * x_a) + (y_a * y_a) + (z_a * z_a));
                double a_change = a_magnitude - old_acc;
                old_acc = a_magnitude;

                if(a_change > threshold){
                    stepCount++;
                    angle.add((int)rotationInDegrees);
                }
                steps.setText(stepCount.toString());

                distance = (float)(stepCount*strideLength);
                dist.setText(String.format("%.2f",distance));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    SensorEventListener magnetometerSensorEventListener = new SensorEventListener() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, a_y, 0, 3);
                    cond_a_y = true;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, mag, 0, 3);
                    cond_mag = true;
                    break;
                default:
                    return;
            }

            if (cond_a_y && cond_mag) {
                float[] identityMatrix = new float[9];
                float[] rotationMatrix = new float[9];
                boolean flag = SensorManager.getRotationMatrix(rotationMatrix, identityMatrix, a_y, mag);

                if (flag) {
                    float[] orientationMatrix = new float[3];
                    SensorManager.getOrientation(rotationMatrix, orientationMatrix);
                    float rotationInRadians = orientationMatrix[0];
                    rotationInDegrees = Math.toDegrees(rotationInRadians);
                    direction.setText("Degrees: "+String.format("%.2f",rotationInDegrees));
                }
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
