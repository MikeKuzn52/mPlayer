package com.mikekuzn.mplayer;

import android.app.Activity;
import android.app.Application;

public class PlayerApp extends Application {
    private PlayerFabric playerFabric;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public PlayerFabric getFabric(Activity activity) {
        if (playerFabric == null) {
            playerFabric = DaggerPlayerFabric.builder()
                    .fabricModule(new FabricModule(getApplicationContext() ,activity))
                    .build();
        }
        return playerFabric;
    }
}
