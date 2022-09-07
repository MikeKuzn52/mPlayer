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
@Component(modules = {FabricModule.class, FabricLogic.class, FabricEntities.class})
interface PlayerFabric {
    void inject(MainActivity mainActivity);
}

@Module
class FabricModule {

    private final Context appContext;
    private final Activity mainActivity;

    FabricModule(Context appContext, Activity mainActivity) {
        this.appContext = appContext;
        this.mainActivity = mainActivity;
    }

    @Singleton  @Provides
    PresenterInter providePresenter(StateLogic logic, PlayerControls playerControls, FoldersLogicInter foldersLogic, MoveFileLogicInter moveFileLogic, LoadingLogicInter loadingLogic, ActivityData activityData) {
        return new Presenter(logic, playerControls, foldersLogic, moveFileLogic, loadingLogic, activityData);
    }

    @Singleton @Provides
    MusicListAdapter provideMusicListAdapter(SongsSortAdapter songs, PlayerControls playerControls) {
        return new MusicListAdapter(mainActivity, songs, playerControls.onSongClick);
    }

    @Singleton @Provides
    FolderListAdapter provideFolderListAdapter(Folders folders, FoldersLogicInter foldersLogic) {
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
    FileScanner provideFileScanner(Bitmap defBitmap) {return new FileScanner(appContext, defBitmap);}

    @Provides
    IconsLoader provideIconsLoader(Songs songs) {
        return new IconsLoader(songs);
    }

    @Provides
    MoveFileLogicInter provideMoveFolder(SongsSortAdapter songs){return new MoveFileLogic(mainActivity, songs);}

    @Singleton  @Provides
    Bitmap provideDefBitmap() {
        return  BitmapFactory.decodeResource(appContext.getResources(), R.drawable.music_icon);
    }
}

@Module
class FabricLogic {

    @Provides
    StateLogic provideLogic(ExchangeInter exchange, SongsSortAdapter songsSortAdapter) {
        return new StateLogic(exchange, songsSortAdapter);
    }

    @Provides
    LoadingLogicInter provideLoadingLogic(Songs songs, Folders folders, Permission permission, Saver saver, FileScanner fileScanner, IconsLoader iconsLoader) {
        return new LoadingLogic(songs, folders, permission, saver, fileScanner, iconsLoader);
    }

    @Provides
    FoldersLogicInter provideFoldersLogic(Folders folders, Songs songs, SongsSortAdapter songsSortAdapter, ExchangeInter exchange) {
        return new FoldersLogic(folders, songs, songsSortAdapter, exchange);
    }

    @Provides
    PlayerControls providePlayerControls(ExchangeInter exchange) {
        return new PlayerControls(exchange);
    }
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

