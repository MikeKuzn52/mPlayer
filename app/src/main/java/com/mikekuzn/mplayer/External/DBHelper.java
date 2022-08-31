package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mikekuzn.mplayer.Entities.AddNewSong;
import com.mikekuzn.mplayer.Entities.Songs;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

interface addLoaded {
    void songsAdd(String path, String title, int duration, Bitmap bmp);
}

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "MikePlayerDB";
    public static final String DB_TABLE_NAME = "SongsList";
    public static final String Columns0 = "_id";
    public static final String Columns1 = "Path";
    public static final String Columns2 = "Icon";
    public static final String Columns3 = "title";
    public static final String Columns4 = "duration";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    // Вызывается если базы данных не существует и её нужно создавать
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("MikeKuzn", "DBHelper onCreate");
        sqLiteDatabase.execSQL("create table " + DB_TABLE_NAME + "(" +
                Columns0 + " integer primary key," +
                Columns1 + " text," +
                Columns2 + " BLOB," +
                Columns3 + " text," +
                Columns4 + " text" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("MikeKuzn", "DBHelper onUpgrade");
    }

    public void load(AddNewSong add) {
        // This code is really slow
        /*
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(DBHelper.DB_TABLE_NAME, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String path = cursor.getString(1);
                byte[] blob = cursor.getBlob(2);
                String title = cursor.getString(3);
                int duration = cursor.getInt(4);
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                add.add(path, title, duration, bmp);
                Log.i("MikeKuzn", "db addFile " + path);
            }
            cursor.close();
        } catch (Exception e) {
            db.delete(DBHelper.DB_TABLE_NAME, null, null);
            Log.i("MikeKuzn", "DB Error " + e);
        }
        close();
        // */
    }

    public void save(Context context, final ArrayList<Songs.songDescription> songs) {
        // This code is really slow
        /*
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBHelper.DB_TABLE_NAME, null, null);
        for (AudioModel.songDescription song : songs) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.Columns1, song.path);
            cv.put(DBHelper.Columns2, bmpToArray(song.bmp));
            cv.put(DBHelper.Columns3, song.title);
            cv.put(DBHelper.Columns4, song.duration);
            db.insert(DBHelper.DB_TABLE_NAME, null, cv);
            Log.i("MikeKuzn", "db save " + song.path);
        }//*/
    }

    byte[] bmpToArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
