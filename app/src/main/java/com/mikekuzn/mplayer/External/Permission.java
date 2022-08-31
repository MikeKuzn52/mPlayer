package com.mikekuzn.mplayer.External;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class Permission {
    private boolean firstAsk = false;
    Activity activity;

    public Permission(Activity activity) {
        Log.i("MikeKuzn", "Permission Constructor");
        this.activity = activity;
    }

    public boolean ask() {
        if (checkPermission()) {
            return true;
        } else {
            if (!firstAsk) {
                firstAsk = true;
                Log.i("MikeKuzn", "Ask and wait permissions");
                returnPermission();
            }
        }
        return false;
    }
    private boolean checkPermission() {
        boolean r1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean r2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.i("MikeKuzn", "Permissions " + r1 +" " + r2);
        return r1 && r2;
    }

    private void returnPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "Запрос прав. Пожалуйста разрешите доступ", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.i("MikeKuzn", "Permission killed");
    }
}
