// mPlaerAidlInterface.aidl
package com.mikekuzn.mplayer;

//parcelable AudioModel;

interface mPlaerAidlInterface {
    int[] getPlayerState();
    void setList(in List<String> pathSongs);
    void setCommand(int id, int numSong);
}