package com.mikekuzn.mplayer.Domain;

public interface MoveFileLogicInter {
    void move(int numFolder, int numCurrentSong);
    void setShowCopyMenu();
    void setCallBack(Lib.callBackBool menuShowCallBack);
}
