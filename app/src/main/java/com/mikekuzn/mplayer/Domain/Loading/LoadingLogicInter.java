package com.mikekuzn.mplayer.Domain.Loading;

public interface LoadingLogicInter {
    public interface CallBack{
        void execute(String bigMessage, boolean foldersReady);
    }
    void setCallBack(CallBack callBack);
}
