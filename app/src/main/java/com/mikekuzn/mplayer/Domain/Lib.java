package com.mikekuzn.mplayer.Domain;

public class Lib {
    public interface callBackBool {
        void execute(boolean val);
    }

    public static String pathToDirectory(String path) {
        int ind = path.lastIndexOf('/');
        String sDel = "/storage/emulated/";
        int indFirst = path.indexOf(sDel);
        // if sDel found, skip it
        indFirst = indFirst == 0 ? sDel.length() : 0;
        if (ind == -1) return "";
        return path.substring(indFirst, ind);
    }

    public static String pathToFileName(String path) {
        int ind = path.lastIndexOf('/');
        if (ind == -1) return null;
        return path.substring(ind, path.length());
    }
}
