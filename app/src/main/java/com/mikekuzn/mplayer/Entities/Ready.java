package com.mikekuzn.mplayer.Entities;

public abstract class Ready {

    boolean ready = false;

    public void setReady() {
        ready = true;
    }

    public abstract int size();

    public abstract int fullSize();
}
