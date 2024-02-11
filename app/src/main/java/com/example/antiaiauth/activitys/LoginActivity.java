package com.example.antiaiauth.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.antiaiauth.R;
import com.example.antiaiauth.activitys.HomeActivity;
import com.example.antiaiauth.antiAiTests.BatteryTest;
import com.example.antiaiauth.antiAiTests.ImageTest;
import com.example.antiaiauth.antiAiTests.BluetoothStatusTest;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 502;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
            } else {
                startLoginProcess();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with login process
                startLoginProcess();
            } else {
                // Permission denied, show a message or retry
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLoginProcess() {
        EditText explanationEditText = new EditText(this);
        BatteryTest batteryTest = new BatteryTest(this, explanationEditText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            batteryTest.runTest(batteryResult -> {
                if (batteryResult) {
                    showImageTest();
                } else {
                    Toast.makeText(LoginActivity.this, "Battery test failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageTest() {
        EditText explanationEditText = new EditText(this);
        ImageTest imageTest = new ImageTest(this, explanationEditText);

        imageTest.runTest(imageResult -> {
            if (imageResult) {
                showBluetoothTest();
            } else {
                Toast.makeText(LoginActivity.this, "Image test failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBluetoothTest() {
        BluetoothStatusTest bluetoothTest = new BluetoothStatusTest(this);

        bluetoothTest.runTest(bluetoothResult -> {
            if (bluetoothResult) {
                // Bluetooth is on, navigate to HomeActivity
                finish();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
                // Bluetooth test failed
                Toast.makeText(LoginActivity.this, "Bluetooth test failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
