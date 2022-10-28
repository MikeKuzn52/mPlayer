package com.mikekuzn.mplayer;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.mikekuzn.mplayer.Presenter.ActivityData;
import com.mikekuzn.mplayer.Presenter.PresenterInter;
import com.mikekuzn.mplayer.databinding.FragmentControlsBinding;

import javax.inject.Inject;

public class ControlsFragment extends Fragment {

    @Inject
    PresenterInter presenter;
    FragmentControlsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((PlayerApp)getActivity().getApplication()).getFabric(getActivity()).inject(this);
        // Inflate the layout for this fragment
        binding = FragmentControlsBinding.inflate(inflater);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    presenter.setCurProgress(seekBar.getProgress());
                }
            }
        });

        presenter.getActivityData().getDataTime().observe(this, new Observer<ActivityData.DataCurrentTime>() {
            @Override
            public void onChanged(@Nullable ActivityData.DataCurrentTime data) {
                binding.CurrentTime.setText(data.sCurrTime);
                binding.TotalTime.setText(data.sDuration);
                binding.seekBar.setMax(data.duration);
                binding.seekBar.setProgress(data.currTime);
                binding.btnPlay.setImageResource(data.playing ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });

        presenter.getActivityData().getDataSong().observe(this, new Observer<ActivityData.DataCurrentSong>() {
            @Override
            public void onChanged(@Nullable ActivityData.DataCurrentSong data) {
                binding.currentSongsText.setText(data.currentSongTitle);
                binding.currentSongIcon.setImageBitmap(data.currentSongBitmap);
            }
        });

        return binding.getRoot();
    }

    public void clickPlay(View view) {presenter.play();}
    public void clickPrevious(View view) { presenter.previous();}
    public void clickNext(View view) {presenter.next();}
}