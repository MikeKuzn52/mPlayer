package com.mikekuzn.mplayer.Domain;

import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.Presenter.ListAdapter;

public class PlayerControls {
    ExchangeInter exchange;

    public PlayerControls(ExchangeInter exchange) {
        this.exchange = exchange;
    }

    public void setCurProgress(int progress) {exchange.transmitCommand(4, progress);}
    public void previous(){exchange.transmitCommand(2, -1);}
    public void next(){exchange.transmitCommand(3, -1);}
    public void play(){exchange.transmitCommand(1, -1);}
    public ListAdapter.OnclickRun onSongClick = new ListAdapter.OnclickRun() {
        @Override
        public void run(int current) {exchange.transmitCommand(1, current);}
    };
}
