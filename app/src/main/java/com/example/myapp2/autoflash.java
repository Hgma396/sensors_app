package com.example.myapp2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class autoflash extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView textView;
    private float ChangedVale;
    private Sensor lightsensor;
    private String camerID;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoflash);

        textView = findViewById(R.id.text1);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            camerID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        else{
            Toast.makeText(this, "Sorry No Light Sensor Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        ChangedVale = sensorEvent.values[0];
        textView.setText(String.valueOf(ChangedVale));

        if (ChangedVale<20){
            try {
                cameraManager.setTorchMode(camerID, true);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                cameraManager.setTorchMode(camerID, false);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,lightsensor,sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }
}