package com.mikekuzn.mplayer.Domain;

import android.annotation.SuppressLint;
import android.util.Log;

import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;

import java.util.Timer;
import java.util.TimerTask;

public class StateLogic {
    private LogicModel activityModel = new LogicModel();

    //  for exchange with player service
    ExchangeInter exchange;
    SongsSortAdapter songs;
    LogicModel.Update update;

    public StateLogic(ExchangeInter exchange, SongsSortAdapter songs) {
        this.exchange = exchange;
        this.songs = songs;

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

    @SuppressLint("DefaultLocale")
    private void timer() {
        int[] playerState = exchange.getPlayerState();
        if (playerState.length != 4) return;
        boolean change = false;
        change |= activityModel.currTime != playerState[1];
        activityModel.currTime = playerState[1];
        activityModel.duration = playerState[2];
        boolean playing = playerState[0] == 0;
        change |= activityModel.playing != playing;
        activityModel.playing = playing;
        String title = songs.getTitle(activityModel.numCurrentSong);
        if (activityModel.numCurrentSong != playerState[3] || activityModel.currentSongTitle != title) {
            Log.i("MikeKuzn", "numCurrentSong " + activityModel.numCurrentSong + "->" + playerState[3]);
            activityModel.numCurrentSong = playerState[3];
            activityModel.currentSongTitle = title;
            activityModel.currentSongBitmap = songs.getBitmap(activityModel.numCurrentSong);
            change = true;
        }
        if (change && update != null) {
            update.execute(activityModel);
        }
    }

}
