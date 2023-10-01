package com.mc2023.template;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class StrideLength extends AppCompatActivity {
    Button go;
    EditText height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stride_activity);
        go = findViewById(R.id.go_button);
        height = findViewById(R.id.height_value);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String h = height.getText().toString();
                int h_val = Integer.parseInt(h);
                int stride_length  = (int) (h_val * 0.415);
                Intent intent = new Intent(v.getContext(), StepCounter.class);
                intent.putExtra("stride",stride_length);
                v.getContext().startActivity(intent);
            }
        });
    }
}
