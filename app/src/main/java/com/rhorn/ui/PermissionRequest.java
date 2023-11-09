package com.rhorn.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PermissionRequest extends AppCompatActivity {
    private static final String LOG_TAG = "[PermissionRequest]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] PERMISSIONS = permissionArrayGet();
        permissionsAsk(PERMISSIONS);
        finish();  // self kill activity
    }
    public String[] permissionArrayGet() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            return new String[] {
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
        return new String[]{
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO,
        };
    }

    protected void permissionsAsk(String[] PERMISSIONS) {

        ActivityResultLauncher<String[]> mRequestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            // Handle activity result here

            Log.d(LOG_TAG, result.toString());  // current permission

            String msgOK = "Got permission.";
            String msgFail = "No storage access. APP EXIT";

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.w(LOG_TAG + " ::Perm:: ", msgOK);

            } else if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                Log.w(LOG_TAG + " ::Perm:: ", msgOK);

            } else {
                Toast.makeText(PermissionRequest.this, msgFail, Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG + " ::fail::", msgFail);
            }
        });
        mRequestPermissionsLauncher.launch(PERMISSIONS);
    }

}