package com.mikekuzn.mplayer;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.Presenter.ActivityData;
import com.mikekuzn.mplayer.Presenter.FolderListAdapter;
import com.mikekuzn.mplayer.Presenter.MusicListAdapter;
import com.mikekuzn.mplayer.Presenter.PresenterInter;
import com.mikekuzn.mplayer.databinding.ActivityMainBinding;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject PresenterInter presenter;
    @Inject FolderListAdapter folderListAdapter;
    @Inject MusicListAdapter musicListAdapter;
    // **************************************************
    ActivityMainBinding binding;
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // **************************************************
        ((PlayerApp)getApplication()).getFabric(this).inject(this);
        binding.foldersList.setAdapter(folderListAdapter);
        binding.songsList.setAdapter(musicListAdapter);

        ((PlayerApp)getApplication()).getFabric(this).inject(this);
        binding.foldersList.setAdapter(folderListAdapter);
        binding.songsList.setAdapter(musicListAdapter);
        // **************************************************
        presenter.getActivityData().getBigMassage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String bigMassage) {
                //Log.i("MikeKuzn", "setMsg " + bigMassage);
                binding.bigTextMessage.setText(bigMassage);
                binding.bigTextMessage.setVisibility(bigMassage == null ? View.GONE : View.VISIBLE);
                if (presenter.getActivityData().foldersReady) {
                    //Log.i("MikeKuzn", "notifyDataSetChanged F=" + folderListAdapter.getCount() + " M=" + musicListAdapter.getCount());
                    folderListAdapter.notifyDataSetChanged();
                    musicListAdapter.notifyDataSetChanged();
                }
            }
        });

        presenter.getActivityData().getShowSongs().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean showSongs) {
                binding.foldersList.setVisibility(!showSongs ? View.VISIBLE : View.GONE);
                binding.controls.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                binding.reMove.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                binding.songsList.setVisibility(showSongs ? View.VISIBLE : View.GONE);
                if (showSongs) {
                    Log.i("MikeKuzn", "showSongs true");
                    musicListAdapter.notifyDataSetChanged();
                }
                binding.controls.invalidate();
            }
        });


        presenter.getActivityData().getNumCurrentSong().observe(this, new Observer<ActivityData.NumCurrentSong>() {
            @Override
            public void onChanged(@Nullable ActivityData.NumCurrentSong data) {
                musicListAdapter.setCurrentPosition(data.numCurrentSong);
                binding.songsList.smoothScrollToPosition(data.numCurrentSong);
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
            binding.copyMenuContainer.addView(copyButtons[numFolder]);
        }

        presenter.setMenuShowCallBack(new Lib.callBackBool() {
            @Override
            public void execute(boolean showCopyMenu) {
                Log.i("MikeKuzn", "showCopyMenu " + showCopyMenu);
                for (int numFolder = 0; numFolder < copyButtons.length; numFolder++) {
                    copyButtons[numFolder].setVisibility(showCopyMenu ? View.VISIBLE : View.GONE);
                }
                binding.copyMenuShowButton.setImageResource(showCopyMenu ? R.drawable.pop_up_menu_down : R.drawable.pop_up_menu_up);
            }
        });
    }
    // ****************************************************************************************************
    public void clickShowCopyMenu(View view) {
        presenter.setShowCopyMenu();
    }
}
