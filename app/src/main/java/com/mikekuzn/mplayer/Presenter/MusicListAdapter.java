package com.mikekuzn.mplayer.Presenter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikekuzn.mplayer.Domain.Lib;
import com.mikekuzn.mplayer.Entities.SongsSortAdapter;
import com.mikekuzn.mplayer.R;

public class MusicListAdapter extends ListAdapter {

    SongsSortAdapter songs;
    int currentPosition;

    public MusicListAdapter(Activity activity, SongsSortAdapter songs, OnclickRun onclickRun) {
        super(activity, onclickRun);
        Log.i("MikeKuzn", "MusicListAdapter CONSTRUCTOR ");
        this.songs = songs;
    }

    @Override
    public int getCount() {return songs.size();}

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        View view = coverView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.songs_item, parent, false);
        }
        TextView text = view.findViewById(R.id.music_title_text);
        TextView directory = view.findViewById(R.id.music_second_text);
        TextView duration = view.findViewById(R.id.music_title_duration);
        ImageView icon = view.findViewById(R.id.icon);

        text.setText(songs.getTitle(position));
        text.setTypeface(Typeface.defaultFromStyle(position == currentPosition ? Typeface.BOLD: Typeface.NORMAL));
        directory.setText(Lib.pathToDirectory(songs.getPath(position)));
        int val = songs.getDuration(position) / 1000;
        duration.setText(String.format("%03d:%02d", val / 60, val % 60));
        Bitmap art = songs.getBitmap(position);
        icon.setImageBitmap(art);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Log.i("MikeKuzn", "MusicListAdapter onClick " + position);onclickRun.run(position); notifyDataSetChanged();}
        });
        return view;
    }

    public void setCurrentPosition(int position) {currentPosition = position; notifyDataSetChanged();}
}
