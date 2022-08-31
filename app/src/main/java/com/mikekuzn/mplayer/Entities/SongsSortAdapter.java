package com.mikekuzn.mplayer.Entities;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SongsSortAdapter {

    private ArrayList<Songs.songDescription> songs = new ArrayList<>();

    public void clear() {songs.clear();}

    public void add(Songs.songDescription song) {songs.add(song);}

    public int size() {return songs.size();}

    public List<String> getPathList() {
        ArrayList<String> pathList = new ArrayList<>();
        for (Songs.songDescription s : songs) {
            pathList.add(s.path);
        }
        return pathList;
    }

    public String getPath(int index) {
        if (songs == null || index >= songs.size()) {
            Log.e("MikeKuzn", "getPath id=" + index);
            return null;
        }
        return songs.get(index).path;
    }

    public String getTitle(int index) {
        if (songs == null || index >= songs.size()) {
            //Log.e("MikeKuzn", "getTitle id=" + index);
            return null;
        }
        return songs.get(index).title;
    }

    public int getDuration(int index) {
        if (index >= songs.size()) {
            Log.e("MikeKuzn", "getDuration id=" + index);
            return 0;
        }
        return songs.get(index).duration;
    }
    public Bitmap getBitmap(int index) {
        if (index >= songs.size()) {
            Log.e("MikeKuzn", "getBitmap id=" + index);
            return null;
        }
        return songs.get(index).bmp;
    }
}
