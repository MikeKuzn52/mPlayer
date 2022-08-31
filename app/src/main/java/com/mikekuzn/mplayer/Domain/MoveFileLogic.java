package com.mikekuzn.mplayer.Domain;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.R;

import java.io.File;

public class MoveFileLogic implements MoveFileLogicInter {
    Activity activity;
    SongsSortAdapter songsList;
    Lib.callBackBool callBack;
    Boolean showMenu = false;
    boolean permission = false;

    @Override
    public void setShowCopyMenu() {callBack.execute(showMenu = !showMenu);}

    @Override
    public void setCallBack(Lib.callBackBool callBack) {(this.callBack = callBack).execute(showMenu);}

    @Override
    public void move(int numFolder, int numCurrentSong) {
        if (!permission) {
            if (!askPermission()) {
                return;
            };
        }
        String mainPath = getInternalDirectoryPath();
        for (String p:destinationPath) {
            if (p == null) {
                showAlertDialog();
                return;
            }
            File dir = new File(mainPath + p);
            if(!dir.exists()) {
                Log.i("MikeKuzn", "No exists " + mainPath + p);
                if (!dir.mkdirs()) {
                    Log.i("MikeKuzn", "No ***1");
                    Toast.makeText(activity, "Can not make dir", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("MikeKuzn", "No ***2");
            }
        }
        String pathSong = songsList.getPath(numCurrentSong);
        Rename(pathSong, getInternalDirectoryPath() + destinationPath[numFolder] + Lib.pathToFileName(pathSong));
    }

    String []destinationPath = {"/music/1", "/music/2","/music/3","/music/4","/music/5"};

    public MoveFileLogic(Activity activity, SongsSortAdapter songsSortAdapter) {
        Log.i("MikeKuzn", "MoveFileLogic Constructor");
        this.activity = activity;
        this.songsList = songsSortAdapter;
    }

    private boolean askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                activity.startActivity(new Intent()
                        .setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        .setData(Uri.fromParts("package", activity.getPackageName(), null)));
                return false;
            }
        }
        return true;
    }

    private void Rename(String path1, String path2) {
        Log.i("MikeKuzn", "Rename " + path1 + " -> " + path2);
        File from = new File(path1);
        File to = new File(path2);
        from.renameTo(to);
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.alertFoldersTitle);
        ConstraintLayout cl = (ConstraintLayout) activity.getLayoutInflater().inflate(R.layout.alert_folders, null);
        /*builder.setPositiveButton(R.string.alertButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText sD = ((AlertDialog) dialogInterface).findViewById(R.id.setDistance);
                if (sD != null && !sD.getText().toString().equals("")) {
                    String s = sD.getText().toString();
                    uLogic.setDistanceTo(Integer.parseInt(s));
                }
            }
        });*/
        builder.setView(cl);
        builder.show();
    }

    private String getInternalDirectoryPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
