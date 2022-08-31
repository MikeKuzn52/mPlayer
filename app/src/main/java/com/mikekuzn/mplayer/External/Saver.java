package com.mikekuzn.mplayer.External;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import com.mikekuzn.mplayer.Entities.Songs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Saver{
    Context appContext;
    public Saver(Context appContext) {this.appContext = appContext;}

    public void load(Songs songs) {

    }

    public void save(Songs songs) {
/*        Log.i("MikeKuzn", "***1");
        Parcel parcel = Parcel.obtain();
        int size = songs.fullSize();
        parcel.writeInt(size);
        write("Songs.save", parcel);
*/
    }

    void write(String file,  Parcel parcel){
        FileOutputStream fos = null;
        try {
            fos = appContext.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(parcel.marshall());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.i("MikeKuzn", "write err1 " + file + " " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("MikeKuzn", "write err2 " + file + " " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    Parcel Read(String file) {
/*        FileInputStream fos = null;
        try {
            Log.i("MikeKuzn", "read *1* " + file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[0xFFFF];
            for (int len = reader.read(buffer); len != -1; len = reader.read(buffer)) {
                os.write(buffer, 0, len);
            }
            //return os.toByteArray();
//            CharBuffer buffer = new CharBuffer();
//            byte[] buff = new byte[0];
//            //fos = appContext.openFileInput(file);
//            int bytesRead = reader.read(buffer);
//            int length = 256;
//            while (length != 0) {
//                char[] buffer = new char[200];
//                int length = reader.read(buffer, 0, 200);
//            }
//            if (buff == null) {
//                return null;
//            }
//            Parcel parcel = null;
//            parcel.readByteArray(buff);
///*
//            Log.i("MikeKuzn", "read *2* " + file);
//            String data = "";
//            while (true) {
//                Log.i("MikeKuzn", "read *3* " + file);
//                String rd = reader.readLine();
//                Log.i("MikeKuzn", "read *4* " + file);
//                if (rd == null) return data;
//                else data += rd;
//            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.i("MikeKuzn", "read err1 " + file + " " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("MikeKuzn", "read err2 " + file + " " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return null;
    }
}
