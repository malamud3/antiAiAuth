package com.example.antiaiauth.antiAiTests;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.antiaiauth.R;

public class ImageTest {

    private final AppCompatActivity mActivity;
    private EditText mExplanationEditText;

    public ImageTest(AppCompatActivity activity, EditText explanationEditText) {
        mActivity = activity;
        mExplanationEditText = explanationEditText;
    }

    public void runTest(TestResultCallback callback) {
        AlertDialog.Builder builder = createAlertDialog();

        builder.setPositiveButton("Next", (dialog, which) -> {
            String userResponse = getUserResponse();
            boolean isHappy = checkUserResponse(userResponse);
            callback.onTestResult(isHappy);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> callback.onTestResult(false));
        builder.show();
    }

    private AlertDialog.Builder createAlertDialog() {
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_image_test, null);
        ImageView imageView = dialogView.findViewById(R.id.imageView);
        mExplanationEditText = dialogView.findViewById(R.id.imgTest_editText);

        setImage(imageView);
        return new AlertDialog.Builder(mActivity)
                .setTitle("Image Test")
                .setView(dialogView);
    }

    private String getUserResponse() {
        return mExplanationEditText.getText().toString().trim(); // Removed toLowerCase() for case-insensitive comparison
    }

    private boolean checkUserResponse(String response) {
        return !TextUtils.isEmpty(response) && response.trim().equalsIgnoreCase("happy");
    }

    private void setImage(ImageView imageView) {
        // Set up the ImageView here
        imageView.setImageResource(R.drawable.man_person_icon);
    }

    public interface TestResultCallback {
        void onTestResult(boolean result);
    }
}
