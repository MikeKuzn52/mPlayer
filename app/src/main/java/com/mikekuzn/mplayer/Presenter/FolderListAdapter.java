package com.mikekuzn.mplayer.Presenter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikekuzn.mplayer.Entities.Folders;
import com.mikekuzn.mplayer.R;

public class FolderListAdapter extends ListAdapter {

    Folders folders;

    public FolderListAdapter(Activity activity, Folders folders, OnclickRun onclickRun) {
        super(activity, onclickRun);
        Log.i("MikeKuzn", "FolderListAdapter CONSTRUCTOR " + onclickRun);
        this.folders = folders;
    }

    @Override
    public int getCount() {return folders.size(); }

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        View view = coverView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.folder_item, parent, false);
        }
        TextView name = view.findViewById(R.id.folder_name);
        TextView folder = view.findViewById(R.id.folder);
        TextView number = view.findViewById(R.id.number_musics);

        Pair<String, String> folderName = folders.getPathName(position);
        name.setText(folderName.first);
        folder.setText(folderName.second);
        number.setText(String.format("%d", folders.getCount(position)));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Log.i("MikeKuzn", "FolderListAdapter onClick " + position);if (onclickRun != null) onclickRun.run(position);}
        });
        return view;
    }
}
