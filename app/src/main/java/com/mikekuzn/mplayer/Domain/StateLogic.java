package com.mikekuzn.mplayer.Domain;

import android.util.Log;

import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.External.SaveSettings;

import java.util.Timer;
import java.util.TimerTask;

public class StateLogic {
    private LogicModel activityModel = new LogicModel();
    enum StartLoading {NEED_LOAD, WAIT_FILE_LIST, READY};
    StartLoading startLoading = StartLoading.NEED_LOAD;
    // for exchange with player service
    ExchangeInter exchange;
    SongsSortAdapter songs;
    LogicModel.Update update;
    SaveSettings saveSettings;
    LogicModel.OpenSong openSong;

    public StateLogic(ExchangeInter exchange, SongsSortAdapter songs, SaveSettings saveSettings, LogicModel.OpenSong openSong) {
        this.exchange = exchange;
        this.songs = songs;
        this.saveSettings = saveSettings;
        this.openSong = openSong;

        Log.i("MikeKuzn", "Logic Constructor");
        Timer myTimer = new Timer(); // Создаем таймер
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                timer();
            }
        }, 0L, 500); // интервал - 100 миллисекунд, 500 миллисекунд до первого запуска.
    }

    public void setUpdate(LogicModel.Update update) {
        this.update = update;
    }

    private void timer() {
        if (startLoading != StartLoading.READY) {
            int[] numLoadedCurrentSong = saveSettings.loadNumSong();
            // flag ready is need for waiting the end of loading songs
            boolean ready = openSong.openSong(numLoadedCurrentSong[0], numLoadedCurrentSong[1],
                    saveSettings.loadPlay(),
                    saveSettings.loadCurrentPos()
            );
            if (ready) startLoading = StartLoading.READY;
        }
        int[] playerState = exchange.getPlayerState();
        if (playerState.length != 5) return;
        boolean change = false;
        if (activityModel.currTime != playerState[1]) {
            change = true;
            activityModel.currTime = playerState[1];
            activityModel.duration = playerState[2];
            saveSettings.saveCurrentPos(activityModel.currTime);
        }

        boolean playing = playerState[0] != 0;
        if (activityModel.playing != playing) {
            change = true;
            activityModel.playing = playing;
            saveSettings.savePlay(playing);
        }

        String title = songs.getTitle(activityModel.numCurrentSong);
        if (activityModel.numCurrentSong != playerState[3] ||
                !title.equals(activityModel.currentSongTitle) ) {
            Log.i("MikeKuzn", "numCurrentSong " + activityModel.numCurrentSong + "->" + playerState[3]);
            activityModel.numCurrentSong = playerState[3];
            Log.i("MikeKuzn", "**** save  " + activityModel.numCurrentSong + " " + Lib.littleHash(songs.getPath(activityModel.numCurrentSong)) + songs.getPath(activityModel.numCurrentSong));
            saveSettings.saveNumSong(
                    activityModel.numCurrentSong,
                    Lib.littleHash(songs.getPath(activityModel.numCurrentSong))
            );
            activityModel.currentSongTitle = title;
            activityModel.currentSongBitmap = songs.getBitmap(activityModel.numCurrentSong);
            change = true;
        }
        if (change && update != null) {
            update.execute(activityModel);
        }
    }

}
