package com.mikekuzn.mplayer.Presenter;

import android.util.Log;

import com.mikekuzn.mplayer.Domain.FoldersLogicInter;
import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.Domain.Loading.LoadingLogicInter;
import com.mikekuzn.mplayer.Domain.LogicModel;
import com.mikekuzn.mplayer.Domain.MoveFileLogicInter;
import com.mikekuzn.mplayer.Domain.PlayerControls;
import com.mikekuzn.mplayer.Domain.StateLogic;


public class Presenter implements PresenterInter {
    // **************************************************
    private PlayerControls playerControls;
    FoldersLogicInter foldersLogic;
    MoveFileLogicInter moveFileLogic;
    private final ActivityData activityData;
    public final ActivityData getActivityData() {return activityData;}

    public Presenter(StateLogic stateLogic, PlayerControls playerControls, FoldersLogicInter foldersLogic, MoveFileLogicInter moveFileLogic, LoadingLogicInter loadingLogic, ActivityData activityData) {
        this.activityData = activityData;
        loadingLogic.setCallBack(loadingResultProc);
        foldersLogic.setCallBack(setShowSongsCallBack);
        stateLogic.setUpdate(updateActivity);
        Log.i("MikeKuzn", "Presenter Constructor");
        this.playerControls = playerControls;
        this.foldersLogic = foldersLogic;
        this.moveFileLogic = moveFileLogic;
    }
    // ****************************************************************************************************
    Lib.callBackBool setShowSongsCallBack = new Lib.callBackBool() {
        @Override public void execute(boolean showSongs) {activityData.setShowSongs(showSongs);}
    };
    LoadingLogicInter.CallBack loadingResultProc = new LoadingLogicInter.CallBack() {
        @Override public void execute(String bigMessage, boolean foldersReady) {activityData.setMsg(bigMessage, foldersReady);}
    };
    LogicModel.Update updateActivity = new LogicModel.Update() {
        @Override public void execute(LogicModel info) {activityData.Decode(info);}
    };
    // ****************************************************************************************************
    // Jumpers (для упрощения связь с playerControls напрямую без интерфейса. Т.е. отклонение от SOLID)
    @Override public void setCurProgress(int progress) {playerControls.setCurProgress(progress);}
    @Override public void play() {playerControls.play();}
    @Override public void previous() {playerControls.previous();}
    @Override public void next() {playerControls.next();}
    @Override public boolean onBackPressed() {return foldersLogic.goToFolders();}
    @Override public void onCopyClick(int numFolder) {
        int numCurrentSong = activityData.getDataSong().getValue().numCurrentSong;
        moveFileLogic.move(numFolder, numCurrentSong);
    }

    @Override
    public void setMenuShowCallBack(Lib.callBackBool menuShowCallBack) {moveFileLogic.setCallBack(menuShowCallBack);}

    @Override
    public void setShowCopyMenu() {moveFileLogic.setShowCopyMenu();}
    // ****************************************************************************************************
}
