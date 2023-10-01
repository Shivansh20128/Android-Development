package com.mc2023.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

import java.util.ArrayList;

public class DrawDots extends View {
    ArrayList<Float> points_x = new ArrayList<>();
    ArrayList<Float> points_y = new ArrayList<>();

    private final Paint paint = new Paint();
    public DrawDots(Context context) {
        super(context);
    }

    public DrawDots(MyCanvas context, ArrayList<Float> x_corr, ArrayList<Float> y_corr) {
        super(context);
        points_x.addAll(x_corr);
        points_y.addAll(y_corr);

    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setTextSize(50);
        canvas.drawCircle(525,1100,10,paint);
        canvas.drawText("S",550,1100,paint);

//        System.out.println("size: "+points_x.size());

        for(int i=0;i< points_x.size();i++){
//            System.out.println("x: "+points_x.get(i) + " , y: "+points_y.get(i)+"\n");
            canvas.drawCircle(points_x.get(i),points_y.get(i),10,paint);
        }
        if(points_x.size()>0){
            canvas.drawText("D",points_x.get(points_x.size()-1)+20,points_y.get(points_y.size()-1)+20,paint);
        }

    }
}
