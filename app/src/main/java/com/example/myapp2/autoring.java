package com.example.myapp2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class autoring extends AppCompatActivity {

    private static final int REQUEST_MICROPHONE = 1;
    private MediaRecorder mediaRecorder;
    private Handler handler;
    private AudioManager audioManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
        } else {
            startNoiseMonitoring();
        }
    }

    private void startNoiseMonitoring() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adjustVolumeBasedOnNoiseLevel();
                handler.postDelayed(this, 1000); // Repeat every second
            }
        }, 1000);
    }

    private void adjustVolumeBasedOnNoiseLevel() {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        double noiseLevel = getNoiseLevel();
        int newVolume;

        if (noiseLevel < 30) {
            newVolume = 0; // Silent
        } else if (noiseLevel < 60) {
            newVolume = maxVolume / 2; // Medium
        } else {
            newVolume = maxVolume; // Loud
        }

        audioManager.setStreamVolume(AudioManager.STREAM_RING, newVolume, AudioManager.FLAG_SHOW_UI);
    }

    private double getNoiseLevel() {
        if (mediaRecorder != null) {
            return 20 * Math.log10(mediaRecorder.getMaxAmplitude() / 2700.0);
        } else {
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startNoiseMonitoring();
            } else {
                Toast.makeText(this, "Microphone permission is required to monitor noise levels.", Toast.LENGTH_SHORT).show();
            }
        }
    }



}











