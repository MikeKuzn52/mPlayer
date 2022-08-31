package com.mikekuzn.mplayer;


// 8 сохранять номер папки, номер пестни и путь в настройки
// 10 Попробовать сохранять в бинарь
// 11 Сделать перемиещение в преднастроенные папки
// 12 при наличии входящего звонка отключать музыку, а потом возобновлять
// 13 Сделать часть проекта на котлине
// 14 Загрузку сделать не на треях а на корутинах
// 15 сделать пересканирование медиатеки


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.Presenter.ActivityData;
import com.mikekuzn.mplayer.Presenter.FolderListAdapter;
import com.mikekuzn.mplayer.Presenter.MusicListAdapter;
import com.mikekuzn.mplayer.Presenter.PresenterInter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    PresenterInter presenter;
    @Inject
    FolderListAdapter folderListAdapter;
    @Inject
    MusicListAdapter musicListAdapter;
    // **************************************************
    ListView folderList, songList;
    TextView bigTextMessage, currentSongsText, CurrentTime, TotalTime;
    SeekBar seekBar;
    ImageButton btnPlay;
    ImageView currentSongIcon;
    RelativeLayout mainLayout, controls, reMove;
    LinearLayout copy_menu_container;
    ImageView copy_menu_show_button;
    ImageView[] copyButtons;
    // ****************************************************************************************************
    @Override
    public void onBackPressed() {
        if (presenter.onBackPressed()) {
            super.onBackPressed();
        }
    }
    // ****************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MikeKuzn", "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllById();
        // **************************************************
        ((PlayerApp)getApplication()).getFabric(this).inject(this);
        folderList.setAdapter(folderListAdapter);
        songList.setAdapter(musicListAdapter);

        // **************************************************
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    presenter.setCurProgress(seekBar.getProgress());
                }
            }
        });
        // **************************************************
        presenter.getActivityData().getBigMassage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String bigMassage) {
                //Log.i("MikeKuzn", "setMsg " + bigMassage);
                bigTextMessage.setText(bigMassage);
                bigTextMessage.setVisibility(bigMassage == null ? View.GONE : View.VISIBLE);
                if (bigMassage == null) {
                    //Log.i("MikeKuzn", "notifyDataSetChanged F=" + folderListAdapter.getCount() + " M=" + musicListAdapter.getCount());
                    folderListAdapter.notifyDataSetChanged();
                    musicListAdapter.notifyDataSetChanged();
                }
            }
        });

        presenter.getActivityData().getShowSongs().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean showSongs) {
                folderList.setVisibility(!showSongs ? View.VISIBLE : View.GONE);
                controls.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                reMove.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                songList.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                if (showSongs) {
                    Log.i("MikeKuzn", "showSongs true");
                    musicListAdapter.notifyDataSetChanged();
                }
                controls.invalidate();
            }
        });

        presenter.getActivityData().getDataTime().observe(this, new Observer<ActivityData.DataCurrentTime>() {
            @Override
            public void onChanged(@Nullable ActivityData.DataCurrentTime data) {
                CurrentTime.setText(data.sCurrTime);
                TotalTime.setText(data.sDuration);
                seekBar.setMax(data.duration);
                seekBar.setProgress(data.currTime);
                btnPlay.setImageResource(data.playing ? R.drawable.ic_play : R.drawable.ic_pause);

            }
        });
        presenter.getActivityData().getDataSong().observe(this, new Observer<ActivityData.DataCurrentSong>() {
            @Override
            public void onChanged(@Nullable ActivityData.DataCurrentSong data) {
                currentSongsText.setText(data.currentSongTitle);
                currentSongIcon.setImageBitmap(data.currentSongBitmap);
                musicListAdapter.setCurrentPosition(data.numCurrentSong);
                songList.smoothScrollToPosition(data.numCurrentSong);
            }
        });

        copyButtons = new ImageButton[5];
        int wh = getResources().getDimensionPixelSize(R.dimen.bigIconSize);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh, wh);
        for (int numFolder = 0; numFolder < copyButtons.length; numFolder++) {
            copyButtons[numFolder] = new ImageButton(this);
            copyButtons[numFolder].setLayoutParams(lp);
            copyButtons[numFolder].setImageResource(R.drawable.ic_copy);
            copyButtons[numFolder].setBackgroundColor(0);
            final int lNumFolder = numFolder;
            copyButtons[numFolder].setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {presenter.onCopyClick(lNumFolder);}
            });
            copy_menu_container.addView(copyButtons[numFolder]);
        }

        presenter.setMenuShowCallBack(new Lib.callBackBool() {
            @Override
            public void execute(boolean showCopyMenu) {
                Log.i("MikeKuzn", "showCopyMenu " + showCopyMenu);
                for (int numFolder = 0; numFolder < copyButtons.length; numFolder++) {
                    copyButtons[numFolder].setVisibility(showCopyMenu ? View.VISIBLE : View.GONE);
                }
                copy_menu_show_button.setImageResource(showCopyMenu ? R.drawable.pop_up_menu_down : R.drawable.pop_up_menu_up);
            }
        });
    }
    // ****************************************************************************************************
    public void clickPlay(View view) {presenter.play();}
    public void clickPrevious(View view) { presenter.previous();}
    public void clickNext(View view) {presenter.next();}
    // ****************************************************************************************************
    void findAllById(){
        folderList = findViewById(R.id.foldersList);
        songList = findViewById(R.id.songsList);
        bigTextMessage = findViewById(R.id.bigTextMessage);
        CurrentTime = findViewById(R.id.CurrentTime);
        TotalTime = findViewById(R.id.TotalTime);
        seekBar = findViewById(R.id.seekBar);
        btnPlay = findViewById(R.id.btnPlay);
        currentSongsText = findViewById(R.id.currentSongsText);
        currentSongIcon = findViewById(R.id.currentSongIcon);
        controls = findViewById(R.id.controls);
        reMove = findViewById(R.id.reMove);
        mainLayout = findViewById(R.id.mainLayout);
        copy_menu_container = findViewById(R.id.copy_menu_container);
        copy_menu_show_button = findViewById(R.id.copy_menu_show_button);
    }

    public void clickShowCopyMenu(View view) {
        presenter.setShowCopyMenu();
    }
}
