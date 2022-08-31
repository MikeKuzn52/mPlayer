package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.mikekuzn.mplayer.Entities.AddNewSong;

public class FileScanner {
    Context appContext;
    Bitmap defBitmap;
    public FileScanner(Context appContext, Bitmap defBitmap) {
        this.appContext = appContext;
        this.defBitmap = defBitmap;
    }

    public void execute(AddNewSong add) {
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        Cursor cursor = appContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(0);
            String title = cursor.getString(1);
            int duration = cursor.getInt(2);
            add.add(path, title, duration, defBitmap);
            //Log.i("MikeKuzn", "scan addFile " + path);
        }
        cursor.close();
    }
}
