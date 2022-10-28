package com.mikekuzn.mplayer.Domain;

import com.mikekuzn.mplayer.External.ExchangeInter;
import com.mikekuzn.mplayer.Presenter.ListAdapter;
import com.mikekuzn.mplayer.External.ExchangeInter.command;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerControls {
    ExchangeInter exchange;

    @Inject PlayerControls(ExchangeInter exchange) {
        this.exchange = exchange;
    }

    public void setCurProgress(int progress) {exchange.transmitCommand(command.CUR_POS, progress);}
    public void previous(){exchange.transmitCommand(command.PREVIOUS, -1);}
    public void next(){exchange.transmitCommand(command.NEXT, -1);}
    public void play(){exchange.transmitCommand(command.PLAY, -1);}
    public ListAdapter.OnclickRun onSongClick = new ListAdapter.OnclickRun() {
        @Override
        public void run(int current) {exchange.transmitCommand(command.PLAY, current);}
    };
}
