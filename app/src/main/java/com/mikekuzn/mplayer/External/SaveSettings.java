package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SaveSettings {

    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private static final String NumSongKey = "NumSong";
    private static final String NumSongHashKey = "NumSongHash";
    private static final String PlayKey = "Play";
    private static final String SeekKey = "Seek";

    public SaveSettings(Context context) {
        sharedPrefs = context.getSharedPreferences("mPlayer", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    public void saveNumSong(int num, int hash) {
        editor.putInt(NumSongKey, num);
        editor.putInt(NumSongHashKey, hash);
        //Log.i("MikeKuzn", "saveNumSong " + num + " " + hash);
        editor.apply();
    }

    public void savePlay(boolean play) {
        editor.putBoolean(PlayKey, play);
        editor.apply();
        Log.i("MikeKuzn", "savePlay " + play);
    }

    public void saveSeek(int seek) {
        editor.putInt(SeekKey, seek);
        editor.apply();
        Log.i("MikeKuzn", "saveSeek " + seek);
    }

    public int[] loadNumSong() {
        //Log.i("MikeKuzn", "loadNumSong " + sharedPrefs.getInt(NumSongKey, -1) + " " + sharedPrefs.getInt(NumSongHashKey, 0));
        return new int[] {
                sharedPrefs.getInt(NumSongKey, -1),
                sharedPrefs.getInt(NumSongHashKey, 0)
        };
    }

    public boolean loadPlay() {
        Log.i("MikeKuzn", "loadPlay " + sharedPrefs.getBoolean(PlayKey, false));
        return sharedPrefs.getBoolean(PlayKey, false);
    }
    public int loadSeek() {
        return sharedPrefs.getInt(SeekKey, 0);
    }
}
