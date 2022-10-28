package com.mikekuzn.mplayer.Domain;

import android.util.Log;

import com.mikekuzn.mplayer.Entities.Folders;
import com.mikekuzn.mplayer.Entities.Songs;
import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.Presenter.ListAdapter;

import com.mikekuzn.mplayer.External.ExchangeInter.command;

import java.util.ArrayList;

public class FoldersLogic implements FoldersLogicInter {

    Folders folders;
    boolean isShowSongs = false;
    public ArrayList<Integer> selectedFolders = new ArrayList<>();
    Lib.callBackBool setShowSongsBackBool;
    Songs songs;
    SongsSortAdapter songsSortAdapter;
    ExchangeInter exchange;

    public FoldersLogic(Folders folders, Songs songs, SongsSortAdapter songsSortAdapter, ExchangeInter exchange) {
        Log.i("MikeKuzn", "FoldersLogic Constructor");
        this.folders = folders;
        this.songs = songs;
        this.songsSortAdapter = songsSortAdapter;
        this.exchange = exchange;
    }

    private final ListAdapter.OnclickRun onFolderClick = new ListAdapter.OnclickRun() {
        @Override
        public void run(int numFolder) {
            selectedFolders.clear();
            selectedFolders.add(numFolder);
            setEnabledSongs();
            applyFolder(0, true);
        }
    };

    @Override
    public ListAdapter.OnclickRun getOnFolderClick() {
        return onFolderClick;
    }

    @Override
    public void setCallBack(Lib.callBackBool setShowSongsBackBool) {
        this.setShowSongsBackBool = setShowSongsBackBool;
    }

    @Override
    public boolean goToFolders() {
        if (isShowSongs) {
            isShowSongs = false;
            setShowSongsBackBool.execute(false);
            exchange.transmitCommand(command.STOP, 0);
            return false;
        }
        return true;
    }

    void applyFolder(int numSong, boolean reStart) {
        Log.i("MikeKuzn", "startFolderPlay");
        if (setShowSongsBackBool != null) {
            exchange.transmitList(songsSortAdapter.getPathList());
            isShowSongs = true;
            setShowSongsBackBool.execute(true);
            if (reStart) {
                exchange.transmitCommand(command.PLAY, numSong);
            }
        }
    }

    void setEnabledSongs() {
        songsSortAdapter.clear();
        for (int i = 0; i < songs.fullSize(); i++){
            String soundFolder = Lib.pathToDirectory(songs.getPath(i));
            for (Integer index:selectedFolders) {
                String folder = folders.getFolder(index);
                if (soundFolder.equals(folder)) {
                    songsSortAdapter.add(songs.get(i));
                }
            }
        }
        Log.i("MikeKuzn", "setEnabledSongs all-" + songs.size() + " selected-" + songsSortAdapter.size());
    }

    @Override
    public boolean openSong(int numSong, int hash, boolean reStart, int currentPos) {
        if (folders.size() == 0) { // (if songs loaded but icons is not loaded, then folders.ready=true and songs.ready=false)
            // Loading is not ready. Try again
            Log.i("MikeKuzn", "Loading is not ready. Try again " + songs.size() + " " + songs.fullSize());
            return false;
        }
        // The songs.fullSize() need for don't wait the end of loading songs and icons. Only waiting the end of loading songs
        for (int nSong = 0;  nSong < songs.fullSize(); nSong++) {
            if (hash == Lib.littleHash(songs.getPath(nSong))) {
                String folder = Lib.pathToDirectory(songs.getPath(nSong));
                for (int numFolder = 0;  numFolder < folders.size(); numFolder++) {
                    if (folders.equalsFolderPath(numFolder, folder)) {
                        selectedFolders.clear();
                        selectedFolders.add(numFolder);
                        setEnabledSongs();
                        int songHash = Lib.littleHash(songsSortAdapter.getPath(numSong));
                        if (songHash == hash) {
                            Log.i("MikeKuzn", "Apply folder " + numFolder + " and song " + numSong + " hash=" + songHash + " reStart=" + reStart + " CurrentPos=" + currentPos);
                            applyFolder(numSong, true);
                            exchange.transmitCommand(command.CUR_POS, currentPos);
                            if (!reStart) {
                                // press pause.
                                exchange.transmitCommand(command.PLAY, -1);
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return true;
    }
}
