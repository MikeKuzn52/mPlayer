package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSettings {

    private SharedPreferences sharedPrefs;
    private static final String NumSongKey = "NumSong";
    private static final String NumSongHashKey = "NumSongHash";

    public SaveSettings(Context context) {
        sharedPrefs = context.getSharedPreferences("mPlayer", Context.MODE_PRIVATE);
    }

    public void saveNumSong(int num, int hash) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(NumSongKey, num);
        editor.putInt(NumSongHashKey, hash);
        //Log.i("MikeKuzn", "saveNumSong " + num + " " + hash);
        editor.apply();
    }

    public int[] loadNumSong() {
        //Log.i("MikeKuzn", "loadNumSong " + sharedPrefs.getInt(NumSongKey, -1) + " " + sharedPrefs.getInt(NumSongHashKey, 0));
        return new int[] {
                sharedPrefs.getInt(NumSongKey, -1),
                sharedPrefs.getInt(NumSongHashKey, 0)
        };
    }
}
