package com.mikekuzn.mplayer.Domain.Loading;

import android.util.Log;

import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.Entities.Folders;
import com.mikekuzn.mplayer.Entities.Songs;
import com.mikekuzn.mplayer.External.FileScanner;
import com.mikekuzn.mplayer.External.LoaderIcons;
import com.mikekuzn.mplayer.External.Permission;
import com.mikekuzn.mplayer.External.Saver;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingLogic implements LoadingLogicInter {
    enum State {
        ASK_PERMISSION,
        READING_DATA,
        SCAN_FILES,
        LOAD_ICONS,
        SAVING_DATA,
        READY
    }
    State state = State.ASK_PERMISSION;
    String bigMessage = "Loading";

    Songs songs;
    Folders folders;
    Permission permission;
    Saver saver;
    FileScanner fileScanner;
    LoaderIcons loaderIcons;
    CallBack callBack;
    Timer myTimer;

    public LoadingLogic(Songs songs, Folders folders, Permission permission, Saver saver, FileScanner fileScanner, LoaderIcons loaderIcons) {
        this.songs = songs;
        this.folders = folders;
        this.permission = permission;
        this.saver = saver;
        this.fileScanner = fileScanner;
        this.loaderIcons = loaderIcons;
        if (songs.isEmpty()) {
            // Starts loading
            myTimer = new Timer(); // Создаем таймер
            myTimer.schedule(new TimerTask() { // Определяем задачу
                @Override
                public void run() {
                    execute();
                }
            }, 0L, 100); // интервал - 100 миллисекунд, 100 миллисекунд до первого запуска.
        }
    }

    @Override
    public void setCallBack(CallBack callBack) {this.callBack = callBack; callBack.execute(bigMessage);}
    private void runCallBack(String lBigMessage) {bigMessage = lBigMessage; if (callBack != null) callBack.execute(bigMessage);}
    // **************************************************
    private void execute() {
        switch (state) {
            case ASK_PERMISSION: // ask and waiting permission to work with files
                runCallBack("Запрос доступа");
                if (permission.ask()) {
                    Log.i("MikeKuzn", "Permissions is ready");
                    state = State.READING_DATA; // permission is ready. Start reading files
                } else {
                    break;
                }
            case READING_DATA:
                runCallBack("Чтение данных");
                saver.load(songs);
                if (!songs.isEmpty()) {
                    state = State.READY;
                    break;
                } else {
                    state = State.SCAN_FILES;
                }
            case SCAN_FILES:
                runCallBack("Cканирование файлов");
                fileScanner.execute(songs);
                if (songs.isEmpty()) {
                    state = State.READY;        // No files found
                    Log.i("MikeKuzn", "No files found");
                    break;
                } else {
                    state = State.LOAD_ICONS;
                    FoldersUpdate();
                    Log.i("MikeKuzn", "Scan files is ready");
                }
            case LOAD_ICONS:
                runCallBack(/*"Cканирование иконок"*/ null);
                if (loaderIcons.execute()) {
                    Log.i("MikeKuzn", "Load Icons is ready");
                    songs.setReady();
                    state = State.SAVING_DATA;
                } else {
                    break;
                }
            case SAVING_DATA:
                saver.save(songs);
                state = State.READY;
                Log.i("MikeKuzn", "Load is ready");
            case READY:
                break;
        }
        // Then state == State.READY this module will be deleted for clear memory
        if (state == State.READY) {
            runCallBack(null);
            Log.i("MikeKuzn", "LoadingLogic stop");
            myTimer.cancel();
            myTimer = null;
            permission = null;
            saver = null;
            fileScanner = null;
            loaderIcons = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.i("MikeKuzn", "LoadingLogic killed");
    }

    private void FoldersUpdate() {
        Log.i("MikeKuzn", "UpdateFolders");
        folders.clear();
        for (int i = 0; i < songs.fullSize(); i++) {
            String folderName = Lib.pathToDirectory(songs.getPath(i));
            boolean find = false;
            for (int j = 0; j < folders.fullSize(); j++) {
                if (folders.equalsFolderPath(j, folderName)) {
                    find = true;
                    folders.countInc(j);
                }
            }
            if (!find) {
                folders.add(folderName);
            }
        }
        folders.setReady();
    }
}
