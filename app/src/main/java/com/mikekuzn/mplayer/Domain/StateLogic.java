package com.mikekuzn.mplayer.Domain;

import android.util.Log;

import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.External.SaveSettings;

import java.util.Objects;
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
            boolean ready = openSong.openSong(numLoadedCurrentSong[0], numLoadedCurrentSong[1], true);
            if (ready) startLoading = StartLoading.READY;
//v0 в StateLogic загружать и сохранять номер песни и хэшкод пути к песне.
//v1 передать в FoldersLogic номер и хэшкод пути песни
//v2 в FoldersLogic перейти на нужную пепку и запустить нужную композицию
//3 добавить хэшкод пути песни в службу. и передавать в StateLogic
//4 если плеер уже проигрывает то пункт 2 с пометкой не запускать мелодию
//5 в FoldersLogic если выходим из проигрывания песен в меню папок останавливать проигрывание
//6 сервис должен передать -1 текущей пести и сохранить его
//7 в логике загрузки готовность songs and folders не дожидаясь загрузки иконок??
//-1 Сначала нужно решить проблемы с загрузкой

        }
        int[] playerState = exchange.getPlayerState();
        if (playerState.length != 5) return;
        boolean change = activityModel.currTime != playerState[1];
        activityModel.currTime = playerState[1];
        activityModel.duration = playerState[2];
        boolean playing = playerState[0] == 0;
        change |= activityModel.playing != playing;
        activityModel.playing = playing;
        String title = songs.getTitle(activityModel.numCurrentSong);
        if (activityModel.numCurrentSong != playerState[3] || !Objects.equals(activityModel.currentSongTitle, title)) {
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
