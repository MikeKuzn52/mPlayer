package com.mikekuzn.mplayer.Domain.Loading;

public interface LoadingLogicInter {
    public interface CallBack{
        void execute(String bigMessage);
    }
    void setCallBack(CallBack callBack);
}
