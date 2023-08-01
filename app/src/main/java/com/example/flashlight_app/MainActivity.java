package com.example.flashlight_app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the device has a camera with a flash
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Your device does not support flashlight.", Toast.LENGTH_SHORT).show();
            finish(); // Close the app if there's no flashlight
            return;
        }

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Button toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlashlight();
                } else {
                    turnOnFlashlight();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Make sure to turn off the flashlight when the app is paused or closed
        if (isFlashOn) {
            turnOffFlashlight();
        }
    }

    private void turnOnFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, true);
            isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
