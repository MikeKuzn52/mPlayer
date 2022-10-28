package com.mikekuzn.mplayer.External;

import java.util.List;

public interface ExchangeInter {
    int[] getPlayerState();

    void transmitCommand(command comId, int dopValue);

    boolean transmitList(List<String> pathList);

    enum command{PLAY, PREVIOUS, NEXT, STOP, CUR_POS};

}
