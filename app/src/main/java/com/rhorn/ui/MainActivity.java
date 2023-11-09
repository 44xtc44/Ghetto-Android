package com.rhorn.ui;

import com.rhorn.R;
import com.rhorn.Ghetto.runGhettoRecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean isBtnGhettoRecorderPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.rhorn.R.layout.activity_main);

        StringBuilder strBuild = new StringBuilder();
        strBuild.append("\n\n\tEnable Chrome Browser and").append("\n\t");
        strBuild.append("Notifications.").append("\n\n\t");
        strBuild.append("Fresh VM needs user/browser\n\tconfirm.").append("\n\n\t");
        strBuild.append("URL \"localhost:1242\"").append("\n\n\n\tSome items are draggable.");
        TextView textView = findViewById(R.id.txtViewMain);
        textView.setTextSize(22);
        textView.setText(strBuild);
    }

    /** GhettoRecorder button */
    public void startGhettoRecorderService(View view) {

        Button btnStartGhetto = findViewById(R.id.btnStartGhetto);
        Button btnStartBrowser = findViewById(R.id.btnStartBrowser);

        if (!isBtnGhettoRecorderPressed) {
            btnStartGhetto.setBackgroundColor(Color.GREEN);
            btnStartGhetto.setTextColor(Color.BLACK);
            btnStartGhetto.setText("\n Ghetto Stop \n");
            isBtnGhettoRecorderPressed = true;

            final Handler handler = new Handler();
            handler.postDelayed(() -> btnStartBrowser.setVisibility(View.VISIBLE), 1000);

            Intent permissionRequest = new Intent(this, PermissionRequest.class);
            startActivity(permissionRequest);

            Intent startIntent = new Intent(this, runGhettoRecorder.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);

        } else {
            btnStartGhetto.setVisibility(View.INVISIBLE);
            btnStartBrowser.setVisibility(View.INVISIBLE);
            TextView textView = findViewById(R.id.txtViewMain);
            textView.setTextSize(22);

            textView.setText("\n\n\tService stopped\n\n\tExit");
            isBtnGhettoRecorderPressed = false;

            // stopService
            Intent stopIntent = new Intent(MainActivity.this, runGhettoRecorder.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }
    }

    /** Browser button /localhost:1242 */
    public void startBrowser(View view) {

        if (isBtnGhettoRecorderPressed) {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:1242"));
            browser.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            final Handler handler = new Handler();
            handler.postDelayed(() -> startActivity(browser), 50);
        }
    }
}