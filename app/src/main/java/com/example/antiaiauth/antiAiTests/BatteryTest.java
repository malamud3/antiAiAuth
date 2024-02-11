package com.example.antiaiauth.antiAiTests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BatteryTest {

    private final AppCompatActivity mActivity;
    private final EditText mExplanationEditText;

    public BatteryTest(AppCompatActivity activity, EditText explanationEditText) {
        mActivity = activity;
        mExplanationEditText = explanationEditText;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void runTest(TestResultCallback callback) {
        AlertDialog.Builder builder = createAlertDialog();

        builder.setPositiveButton("Next", (dialog, which) -> {
            int enteredPercentage = getUserResponse();
            int currentBatteryPercentage = getBatteryPercentage();

            callback.onTestResult(enteredPercentage == currentBatteryPercentage);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> callback.onTestResult(false));
        builder.show();
    }

    private AlertDialog.Builder createAlertDialog() {
        return new AlertDialog.Builder(mActivity)
                .setTitle("Battery Test")
                .setMessage("Before proceeding, please enter the current battery percentage.")
                .setView(mExplanationEditText);
    }

    private int getUserResponse() {
        return Integer.parseInt(mExplanationEditText.getText().toString().trim());
    }

    private int getBatteryPercentage() {
        Intent batteryIntent = mActivity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int) ((level / (float) scale) * 100);
    }

    public interface TestResultCallback {
        void onTestResult(boolean result);
    }
}




