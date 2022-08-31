package com.mikekuzn.mplayer.Presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class ListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    Activity activity;

    public interface OnclickRun {
        void run(int current);
    }
    OnclickRun onclickRun;

    public ListAdapter(Activity activity, OnclickRun onclickRun) {
        this.activity = activity;
        this.onclickRun = onclickRun;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return null;//audioModel.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }
    // ****************************************************************************************************
    Runnable ChangeRun;
    Runnable getChangeRun() {
        if (ChangeRun == null) {
            ChangeRun = new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            };
        }
        return ChangeRun;
    }
}
