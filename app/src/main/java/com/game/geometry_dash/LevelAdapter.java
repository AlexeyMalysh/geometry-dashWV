package com.game.geometry_dash;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LevelAdapter extends ArrayAdapter<String> {

    int length;
    Context mContext;
    SharedPreferences sharedPreferences;

    public LevelAdapter(@NonNull Context context, int length) {
        super(context, R.layout.level_info);
        this.length = length;
        mContext = context;
        sharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.level_info, parent, false);
            mViewHolder.progressText = (TextView) convertView.findViewById(R.id.progressText);
            mViewHolder.levelName = (TextView) convertView.findViewById(R.id.levelName);
            mViewHolder.levelProgress = (ProgressBar) convertView.findViewById(R.id.levelProgress);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // get level progress from shared preferences
        int savedProgress = sharedPreferences.getInt("level" + position + "-progress", 0);
        String placeholder = "Level " + (position + 1);
        String progressText = "Progress: " + savedProgress + "%";
        mViewHolder.levelName.setText(placeholder);
        mViewHolder.levelProgress.setProgress(savedProgress);
        mViewHolder.progressText.setText(progressText);

        return convertView;
    }

    // helper class for faster rendering
    static class ViewHolder{
        TextView progressText;
        TextView levelName;
        ProgressBar levelProgress;
    }
}
