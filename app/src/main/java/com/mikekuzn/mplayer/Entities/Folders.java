package com.mikekuzn.mplayer.Entities;

import android.util.Pair;

import java.util.ArrayList;

public class Folders extends Ready {

    public class FolderDescription {
        public String folderPath;
        public int count;
        public FolderDescription(String folderPath, int count) {
            this.folderPath = folderPath;
            this.count = count;
        }
    }

    private ArrayList<FolderDescription> folders = new ArrayList<>();

    public void clear() {
        folders.clear();
    }
    @Override
    public int size() {return ready ? folders.size() : 0;}
    @Override
    public int fullSize() {return folders.size();}

    public int getCount(int index) {return folders.get(index).count;}

    public void add(String folderPath) { folders.add(new FolderDescription(folderPath, 1));}

    public boolean equalsFolderPath(int index, String folderPath) {return folders.get(index).folderPath.equals(folderPath);}

    public void countInc(int index) {
        folders.get(index).count++;
    }

    public String getFolder(Integer index) {return folders.get(index).folderPath;}

    public Pair<String, String> getPathName(int index) {
        String name = "";
        String shortPath = "";
        if (index < folders.size()) {
            String folderPath = folders.get(index).folderPath;
            int ind = folderPath.lastIndexOf('/');
            if (ind == -1) {
                name = folderPath;
                shortPath = " ";
            } else {
                ind++;
                name = folderPath.substring(ind, folderPath.length());
                shortPath = folderPath.substring(0, ind);
            }
        }
        return Pair.create(name, shortPath);
    }
}
