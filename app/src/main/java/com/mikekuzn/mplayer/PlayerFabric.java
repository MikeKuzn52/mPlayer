package com.mikekuzn.mplayer;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mikekuzn.mplayer.Domain.FoldersLogic;
import com.mikekuzn.mplayer.Domain.FoldersLogicInter;
import com.mikekuzn.mplayer.Domain.Loading.LoadingLogic;
import com.mikekuzn.mplayer.Domain.Loading.LoadingLogicInter;
import com.mikekuzn.mplayer.Domain.MoveFileLogic;
import com.mikekuzn.mplayer.Domain.MoveFileLogicInter;
import com.mikekuzn.mplayer.Domain.PlayerControls;
import com.mikekuzn.mplayer.Domain.StateLogic;
import com.mikekuzn.mplayer.Entities.Folders;
import com.mikekuzn.mplayer.Entities.Songs;
import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.External.ExchangeWithService;
import com.mikekuzn.mplayer.External.FileScanner;
import com.mikekuzn.mplayer.External.IconsLoader;
import com.mikekuzn.mplayer.External.Permission;
import com.mikekuzn.mplayer.External.Saver;
import com.mikekuzn.mplayer.Presenter.ActivityData;
import com.mikekuzn.mplayer.Presenter.FolderListAdapter;
import com.mikekuzn.mplayer.Presenter.MusicListAdapter;
import com.mikekuzn.mplayer.Presenter.Presenter;
import com.mikekuzn.mplayer.Presenter.PresenterInter;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Singleton
@Component(modules = {FabricModule.class})
interface PlayerFabric {
    void inject(MainActivity mainActivity);
}

@Module(includes = {FabricEntities.class})
class FabricModule {

    private final Context appContext;
    private final Activity mainActivity;
    private StateLogic logic;
    private PlayerControls playerControls;
    private FoldersLogicInter foldersLogic;
    Bitmap defBitmap;

    FabricModule(Context appContext, Activity mainActivity) {
        this.appContext = appContext;
        this.mainActivity = mainActivity;
        defBitmap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.music_icon);
    }

    @Singleton  @Provides
    PresenterInter providePresenter(StateLogic logic, PlayerControls playerControls, FoldersLogicInter foldersLogic, MoveFileLogicInter moveFileLogic, LoadingLogicInter loadingLogic, ActivityData activityData) {
        return new Presenter(logic, playerControls, foldersLogic, moveFileLogic, loadingLogic, activityData);
    }

    @Provides
    StateLogic provideLogic(ExchangeInter exchange, SongsSortAdapter songsSortAdapter) {
        logic = new StateLogic(exchange, songsSortAdapter);
        return logic;
    }

    @Provides
    LoadingLogicInter provideLoadingLogic(Songs songs, Folders folders, Permission permission, Saver saver, FileScanner fileScanner, IconsLoader iconsLoader) {
        return new LoadingLogic(songs, folders, permission, saver, fileScanner, iconsLoader);
    }

    @Provides
    FoldersLogicInter provideFoldersLogic(Folders folders, Songs songs, SongsSortAdapter songsSortAdapter, ExchangeInter exchange) {
        foldersLogic = new FoldersLogic(folders, songs, songsSortAdapter, exchange);
        return foldersLogic;
    }

    @Provides
    PlayerControls providePlayerControls(ExchangeInter exchange) {
        playerControls = new PlayerControls(exchange);
        return playerControls;
    }

    // ???? **** MusicListAdapter на уровне view, Songs на уровне Entities. Связь view->Entities допустима????
    @Singleton @Provides
    MusicListAdapter provideMusicListAdapter(SongsSortAdapter songs) {
        return new MusicListAdapter(mainActivity, songs, playerControls.onSongClick);
    }

    // ???? **** Связь view->Entities допустима????
    @Singleton @Provides
    FolderListAdapter provideFolderListAdapter(Folders folders) {
        return new FolderListAdapter(mainActivity, folders, foldersLogic.getOnFolderClick());
    }

    @Singleton @Provides
    ExchangeInter provideExchangeWithService() {
        return new ExchangeWithService(appContext);
    }

    @Provides
    Permission providePermission() {
        return new Permission(mainActivity);
    }

    @Provides
    Saver provideSaver() {
        return new Saver(appContext);
    }

    @Provides
    FileScanner provideFileScanner() {
        return new FileScanner(appContext, defBitmap);
    }

    @Provides
    IconsLoader provideIconsLoader(Songs songs) {
        return new IconsLoader(appContext, songs);
    }

    @Provides
    MoveFileLogicInter provideMoveFolder(SongsSortAdapter songs){return new MoveFileLogic(mainActivity, songs);}
}

@Module
class FabricEntities {
    @Singleton @Provides
    Folders provideFolders() {return new Folders();}

    @Singleton @Provides
    Songs provideSongs() {return new Songs();}

    @Singleton @Provides
    SongsSortAdapter provideSongsSortAdapter() {return new SongsSortAdapter();}
}

