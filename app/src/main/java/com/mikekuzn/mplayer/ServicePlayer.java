package com.mikekuzn.mplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.External.ExchangeInter.command;

import java.io.IOException;
import java.util.List;

public class ServicePlayer extends Service {

    List<String> songs;
    int numCurrentSong = -1;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean firstStarted = false;
    boolean repeat = false;     // Settings of repeat after ended list
    int duration = 0;

    public ServicePlayer() {
    }
    private final mPlaerAidlInterface.Stub binder = new mPlaerAidlInterface.Stub() {
        @Override
        public int[] getPlayerState() throws RemoteException {
            // If no songs or playing have never started
            if (songs == null || songs.isEmpty() || !firstStarted) {
                return new int[]{0, 0, 0, 0};
            }
            if (mediaPlayer.isPlaying()) {
                // Почему то mediaPlayer при запросе getDuration если в паузе или переключении на другой трек бросает error (-38, 0) {Attempt to call getDuration in wrong state: mPlayer=0xb400006f27041f40, mCurrentState=4}
                duration = mediaPlayer.getDuration()/1000;
            }
            int[] r = {mediaPlayer.isPlaying() ? 1 : 0,
                    mediaPlayer.getCurrentPosition()/1000,
                    duration,
                    numCurrentSong,
                    Lib.littleHash(songs.get(numCurrentSong))
            };
            return r;
        }

        @Override
        public void setList(List<String> pathSongs) throws RemoteException {
            Log.i("MikeKuzn", "setList " + pathSongs.size());
            songs = pathSongs;
        }

        @Override
        public void setCommand(int id, int second) throws RemoteException {
            command ID = command.values()[id];
            Log.i("MikeKuzn", "setCommand " + ID.toString() + " " + second);
            if (songs == null || songs.isEmpty()) {
                return;
            }
            if (numCurrentSong == -1 && second == -1) {
                startPlaying(0);
            } else {
                switch (ID) {
                    case PLAY:
                        if (second == -1) {
                            if (mediaPlayer.isPlaying()) {
                                Log.i("MikeKuzn", "pause");
                                mediaPlayer.pause();
                            } else {
                                Log.i("MikeKuzn", "start");
                                mediaPlayer.start();
                            }
                        } else {
                            startPlaying(second);
                        }
                        break;
                    case PREVIOUS:
                        startPlaying(numCurrentSong == 0 ?songs.size() - 1 : numCurrentSong - 1);
                        break;
                    case NEXT:
                        startPlaying(numCurrentSong == songs.size() - 1 ? 0 : numCurrentSong + 1);
                        break;
                    case STOP:
                        mediaPlayer.stop();
                        break;
                    case CUR_POS:
                        if (firstStarted) {
                            mediaPlayer.seekTo(second * 1000);
                        }
                        break;
                    default:

                        break;
                }
            }
        }
    };

    void startPlaying(int numSong) {
        try {
            numCurrentSong = numSong;
            Log.i("MikeKuzn", "startPlaying " +  numSong + " " + songs.get(numCurrentSong));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.get(numCurrentSong));
            mediaPlayer.prepare();
            mediaPlayer.start();
            firstStarted = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (numCurrentSong == -1 || songs.isEmpty()) {
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    Log.i("MikeKuzn", "Playing next ignore");
                    return;
                }
                if (numCurrentSong < songs.size() - 1) {
                    startPlaying(numCurrentSong + 1);
                } else if (repeat) {
                    startPlaying(0);
                }
            }
        });
        return binder;
    }
}