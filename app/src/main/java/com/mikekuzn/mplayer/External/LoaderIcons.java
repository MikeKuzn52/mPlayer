package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.mikekuzn.mplayer.Entities.Songs;

public class LoaderIcons {
    Context appContext;
    Songs songs;
    private Thread[] threadsDecodeSongsIcons = null;

    public LoaderIcons(Context appContext, Songs songs) {
        this.appContext = appContext;
        this.songs = songs;
    }

    public boolean execute() {
        if (threadsDecodeSongsIcons == null) {
            startLoading();
            return false;
        }
        // Waiting for end all threads
        for (Thread tr: threadsDecodeSongsIcons) {
            if (tr.isAlive()) {
                return false;
            }
        }
        threadsDecodeSongsIcons = null;
        return true;
    }

    private void startLoading() {
        int songs = this.songs.fullSize();
        // Creating threads for all icon loading proses
        threadsDecodeSongsIcons = new Thread[songs];
        for (int numSong = 0; numSong < songs; numSong++) {
            int lNumSong = numSong;
            threadsDecodeSongsIcons[numSong] = new Thread(() -> {
                Bitmap bitmap = null;
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                try {
                    mmr.setDataSource(this.songs.getPath(lNumSong));
                    byte[] data = mmr.getEmbeddedPicture();
                    // convert the byte array to a bitmap
                    if (data != null) {
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    }
                }
                catch (IllegalArgumentException e) {
                    Log.i("MikeKuzn", "decode icon error");
                }
                if (bitmap != null) {
                    this.songs.setBitmap(lNumSong, bitmap);
                }
            });
            threadsDecodeSongsIcons[numSong].start();
        }
    }
}
