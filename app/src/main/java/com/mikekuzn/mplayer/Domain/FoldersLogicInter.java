package com.mikekuzn.mplayer.Domain;

import com.mikekuzn.mplayer.Presenter.ListAdapter;

public interface FoldersLogicInter {
    public ListAdapter.OnclickRun getOnFolderClick();

    void setCallBack(Lib.callBackBool setShowSongsCallBack);

    boolean goToFolders();

    public boolean openSong(int numSong, int hash, boolean reStart, int seek);
}
