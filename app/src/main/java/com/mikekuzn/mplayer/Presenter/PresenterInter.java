package com.mikekuzn.mplayer.Presenter;

import com.mikekuzn.mplayer.Domain.Lib;

public interface PresenterInter {

    ActivityData getActivityData();

    void setCurProgress(int progress);

    void play();

    void previous();

    void next();

    boolean onBackPressed();

    void onCopyClick(int numFolder);

    void setMenuShowCallBack(Lib.callBackBool menuShowCallBack);

    void setShowCopyMenu();
}
