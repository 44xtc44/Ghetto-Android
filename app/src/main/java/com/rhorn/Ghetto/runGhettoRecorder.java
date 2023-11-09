package com.rhorn.Ghetto;
import com.rhorn.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.chaquo.python.Python;
import com.chaquo.python.PyObject;
import com.chaquo.python.PyException;
import com.chaquo.python.android.AndroidPlatform;
import com.rhorn.ui.Constants;
import com.rhorn.ui.PermissionRequest;

import java.util.Arrays;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;


public class runGhettoRecorder extends Service {

    private static final String LOG_TAG = "[runGhettoRecorder]";
    private Context context;
    private final int NOTIFICATION_ID = Constants.NOTIFICATION_ITEMS.NOTIFICATION_ID;
    private boolean isDestroyed = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        /* Python startup */
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
    }

    private Notification showNotification(String content) {
        String CHANNEL_ID = Constants.NOTIFICATION_ITEMS.CHANNEL_ID;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, "ForeGround Notification",
                            NotificationManager.IMPORTANCE_HIGH));
        }
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ghetto Service")
                .setContentText(content)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        if (Objects.equals(intent.getAction(), Constants.ACTION.STARTFOREGROUND_ACTION)) {
            startForeground(NOTIFICATION_ID, showNotification("Browser button load  ..."));

            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Toast.makeText(context, "Starting service ...", Toast.LENGTH_SHORT).show();
            // start service code
            try {
                doTask();
            } catch (ExecutionException | InterruptedException | IOException e) {
                String errMsg = "RuntimeException in doTask() " + LOG_TAG;
                Log.i(LOG_TAG, errMsg, e);
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            }
        }
        else if (Objects.equals(intent.getAction(), Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(context, "Stop service ...", Toast.LENGTH_SHORT).show();
            // end service code
            stopForeground(true);
            stopSelfResult(startID);
        }

        return START_STICKY;
    }

    void GhettoRecorderStart() {
        /* Python package */
        Python py = Python.getInstance();
        try {
            PyObject mock = py.getModule("ghetto_recorder_run");
            Log.i(LOG_TAG, "Start GhettoRecorder HTTP server ...");
            mock.callAttr("ghetto_main", "None");  // run a pkg that blocks the thread
        }catch (PyException e){
            String msg = "ghetto" + "\n\tcustom error " + e.getMessage();
            Log.d(LOG_TAG, msg);
            Log.i(LOG_TAG, Arrays.toString(e.getStackTrace()));
        }
    }
    private void doTask() throws IOException, ExecutionException, InterruptedException {

        Intent permissionRequest = new Intent(this, PermissionRequest.class);
        permissionRequest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(permissionRequest);

        // Python app in blocked thread; finish thread: we call our HTTP server POST shutdown
        new Thread(this::GhettoRecorderStart).start();

        final String[] notifyUpd = new String[1];
        notifyUpd[0] = "load app ...";

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            // Service loop
            for(int i=0; i <= 100; i++) {
                if(isDestroyed) {
                    Log.w(LOG_TAG, "Leave executorService.");
                    GhettoKillA ghettoKillA = new GhettoKillA();
                    try {
                        notifyUpd[0] = ghettoKillA.ghettoServerKill();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                try {
                    handler.post(() -> {
                        updateNotification(String.valueOf(notifyUpd[0]));  // update Android UI
                    });
                    Thread.sleep(5000);
                    if (i % 2 == 0) {
                        // Log.w(LOG_TAG + " ::AVD:: ", "AVD *Browser* and *App notifications*. Enable them!");
                    }
                    if (i == 100) {
                        i = 0;
                    }
                } catch (InterruptedException e) {
                    Log.i(LOG_TAG + " ::loop:: ", Arrays.toString(e.getStackTrace()));
                }
            }
        });
    }

    private void updateNotification(String data) {
        Notification notification = showNotification(data);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  // A bound service lives only while it serves another component.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        Toast.makeText(context, "Stopping service ...", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "In onDestroy");
    }
}
