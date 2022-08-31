package com.mikekuzn.mplayer.Domain;

import android.util.Log;

import com.mikekuzn.mplayer.Entities.Folders;
import com.mikekuzn.mplayer.Entities.Songs;
import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.Presenter.ListAdapter;

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
        public void run(int position) {
            Log.i("MikeKuzn", "startFolderPlay");
            selectedFolders.clear();
            selectedFolders.add(position);
            if (setShowSongsBackBool != null) {
                setEnabledSongs();
                exchange.transmitList(songsSortAdapter.getPathList());
                isShowSongs = true;
                setShowSongsBackBool.execute(true);
                exchange.transmitCommand(1, 0);
            }
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
            return false;
        }
        return true;
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

}
