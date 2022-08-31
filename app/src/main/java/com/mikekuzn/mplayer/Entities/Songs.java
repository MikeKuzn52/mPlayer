package com.mikekuzn.mplayer.Entities;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class Songs extends Ready implements AddNewSong {

    public class songDescription {
        public String path;
        public String title;
        public int duration;
        public Bitmap bmp;
        public boolean enabled;

        public songDescription(String path, String title, int duration, Bitmap bmp) {
            this.path = path;
            this.title = title;
            this.duration = duration;
            this.bmp = bmp;
        }
    }

    private ArrayList<songDescription> songs = new ArrayList<>();
    // **************************************************
    public boolean isEmpty() {
        return songs.isEmpty();
    }

    public int size() {return ready ? songs.size() : 0;}

    public int fullSize() {return songs.size();}

    @Override
    public void add(String path, String title, int duration, Bitmap bmp) {
        if (new File(path).exists()) {
            songs.add(new songDescription(path, title, duration, bmp));
        }
    }

    public songDescription get(int index) {return songs.get(index);}
    // **************************************************

    public String getPath(int index) {
        if (songs == null || index >= songs.size()) {
            Log.e("MikeKuzn", "getPath id=" + index);
            return null;
        }
        return songs.get(index).path;
    }

    public void setBitmap(int index, Bitmap bitmap) {
        songs.get(index).bmp = bitmap;
    }
}
