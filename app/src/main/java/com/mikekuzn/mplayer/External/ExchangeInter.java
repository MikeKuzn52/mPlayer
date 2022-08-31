package com.mikekuzn.mplayer.External;

import java.util.List;

public interface ExchangeInter {
    int[] getPlayerState();

    void transmitCommand(int comId, int dopValue);

    boolean transmitList(List<String> pathList);
}
