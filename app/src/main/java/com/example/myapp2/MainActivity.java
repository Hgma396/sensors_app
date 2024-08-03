package com.example.myapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    CardView brtns,rng,flsh,shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        brtns= findViewById(R.id.b1);
        rng= findViewById(R.id.r1);
        flsh= findViewById(R.id.f1);
        shake= findViewById(R.id.s1);

        brtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob1 = new Intent(MainActivity.this, autobrightness.class);
                        startActivity(ob1);
            }
        });

        rng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob3 = new Intent(MainActivity.this,autoring.class);
                startActivity(ob3);

            }
        });

        flsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob2 = new Intent(MainActivity.this, autoflash.class);
                startActivity(ob2);
            }
        });

        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob4 = new Intent(MainActivity.this, shaketocall.class);
                startActivity(ob4);
            }
        });


    }

}
