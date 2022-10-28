package com.mikekuzn.mplayer.Presenter;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import com.mikekuzn.mplayer.Domain.LogicModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActivityData {
    public static class DataCurrentTime {
        public int currTime;
        public int duration;
        public String sCurrTime;
        public String sDuration;
        public boolean playing;
    }
    public static class DataCurrentSong {
        public int numCurrentSong = -1;
        public Bitmap currentSongBitmap;
        public String currentSongTitle;
    }
    public static class NumCurrentSong {
        public int numCurrentSong = -1;
    }
    // **************************************************
    private final MutableLiveData<String> bigMassage = new MutableLiveData<>();
    public boolean foldersReady;
    private final MutableLiveData<Boolean> showSongs = new MutableLiveData<>();
    private final MutableLiveData<DataCurrentTime> dataCurrentTime = new MutableLiveData<>();
    private final MutableLiveData<DataCurrentSong> dataCurrentSong = new MutableLiveData<>();
    private final MutableLiveData<NumCurrentSong> numCurrentSong = new MutableLiveData<>();
    // **************************************************
    public LiveData<String> getBigMassage() {return bigMassage;}
    public LiveData<Boolean> getShowSongs() {return showSongs;}
    public LiveData<DataCurrentTime> getDataTime() {return dataCurrentTime;}
    public LiveData<DataCurrentSong> getDataSong() {return dataCurrentSong;}
    public LiveData<NumCurrentSong> getNumCurrentSong() {return numCurrentSong;}
    // **************************************************
    @Inject public ActivityData() {
        bigMassage.setValue("");
        showSongs.setValue(false);
        dataCurrentTime.setValue(new DataCurrentTime());
        dataCurrentSong.setValue(new DataCurrentSong());
        numCurrentSong .setValue(new NumCurrentSong());
    }
    // ****************************************************************************************************
    @SuppressLint("DefaultLocale")
    public void Decode(LogicModel info){
        DataCurrentTime dataT = dataCurrentTime.getValue();
        dataT.currTime = info.currTime;
        dataT.duration = info.duration;
        dataT.sCurrTime = String.format("%03d:%02d", info.currTime / 60, info.currTime % 60);
        dataT.sDuration = String.format("%03d:%02d", info.duration / 60, info.duration % 60);
        dataT.playing = info.playing;
        dataCurrentTime.postValue(dataT);
        DataCurrentSong dataN = dataCurrentSong.getValue();
        NumCurrentSong dataNum = numCurrentSong.getValue();
        if (dataN.numCurrentSong != info.numCurrentSong || dataN.currentSongTitle != info.currentSongTitle) {
            dataN.numCurrentSong = info.numCurrentSong;
            dataN.currentSongBitmap = info.currentSongBitmap;
            dataN.currentSongTitle = info.currentSongTitle;
            dataCurrentSong.postValue(dataN);
            dataNum.numCurrentSong = info.numCurrentSong;
            numCurrentSong.postValue(dataNum);
        }
    }

    void setMsg(String bigMassage, boolean foldersReady) {
        if (bigMassage != this.bigMassage.getValue()) {
            this.foldersReady = foldersReady;
            this.bigMassage.postValue(bigMassage);
        }
    }
    void setShowSongs(boolean showSongs) {this.showSongs.postValue(showSongs);}
}
