package com.mc2023.template;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyCanvas extends AppCompatActivity {
    int stride;
    double origin_y = 1100;
    double origin_x = 525;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canva);
        Intent intent = getIntent();
        ArrayList<Integer> angles = intent.getIntegerArrayListExtra("angles");
        stride = intent.getIntExtra("stride",70);
        drawDots();
        ArrayList<Float> arr_x = new ArrayList<>();
        ArrayList<Float> arr_y = new ArrayList<>();
        for(int i=0;i<angles.size();i++){
            double rad = Math.toRadians(angles.get(i));
            double y_corr = -(stride * Math.cos(rad) / 2) + origin_y;
            double x_corr = (stride * Math.sin(rad) / 2) + origin_x;
            arr_x.add((float) x_corr);
            arr_y.add((float) y_corr);
            origin_y=y_corr;
            origin_x=x_corr;
        }

        drawDots(arr_x,arr_y);




    }
    private void drawDots(ArrayList<Float> x_corr, ArrayList<Float> y_corr) {
        DrawDots drawDots = new DrawDots(this,x_corr,y_corr);
        setContentView(drawDots);
    }

    private void drawDots(){
        DrawDots drawDots = new DrawDots(this);
        setContentView(drawDots);
    }
}
