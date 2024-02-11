package com.example.antiaiauth.antiAiTests;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BluetoothStatusTest {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1001;
    private final AppCompatActivity mActivity;
    private final BluetoothAdapter mBluetoothAdapter;
    private boolean isBluetoothOnBefore ,isBluetoothOnAfter;
    private boolean result;
    public BluetoothStatusTest(AppCompatActivity activity) {
        mActivity = activity;
        BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = null;
            Toast.makeText(activity, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public void runTest(TestResultCallback callback) {
        isBluetoothOnBefore = isBluetoothOn();
        AlertDialog.Builder builder = createAlertDialog();

        builder.setPositiveButton("Done", (dialog, which) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                toggleBluetooth();
            } else {
                Toast.makeText(mActivity, "Bluetooth cannot be controlled programmatically on this device.", Toast.LENGTH_SHORT).show();
            }
             isBluetoothOnAfter = isBluetoothOn();
             result = isBluetoothOnBefore != isBluetoothOnAfter;

            callback.onTestResult(result);
        });

        builder.show();
    }

    private AlertDialog.Builder createAlertDialog() {
        String bluetoothStatus = (isBluetoothOnBefore) ? "on " : "off ";
        String instructions = (isBluetoothOnBefore ) ? "Please turn off Bluetooth to pass the test":
                                                         "Please enable Bluetooth to pass the test" ;

        return new AlertDialog.Builder(mActivity)
                .setTitle("Bluetooth Test")
                .setMessage("Your Bluetooth is " + bluetoothStatus + instructions);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void toggleBluetooth() {
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSION);
                    return;
                }
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        }
    }

    private boolean isBluetoothOn() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    public interface TestResultCallback {
        void onTestResult(boolean result);
    }
}
