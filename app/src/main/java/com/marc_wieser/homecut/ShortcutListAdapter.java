package com.marc_wieser.homecut;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marc_wieser.homecut.content_provider.HomecutContract;

import org.apache.commons.io.FileUtils;

/**
 * Created by marc on 14/10/2016.
 */

public class ShortcutListAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public ShortcutListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.shortcut_list_item, parent, false);
        ShortcutViewHolder vh = new ShortcutViewHolder(v);
        v.setTag(vh);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ShortcutViewHolder vh = (ShortcutViewHolder) view.getTag();

        vh.name.setText(cursor.getString(cursor.getColumnIndex(HomecutContract.ShortcutEntry.COLUMN_NAME)));
        //TODO : set icon with shortcut type
    }

    private static class ShortcutViewHolder{
        private View itemView;
        private TextView name, size;
        private ImageView icon;

        ShortcutViewHolder(View v){
            itemView = v;
            name = (TextView) v.findViewById(R.id.filename);
            size = (TextView) v.findViewById(R.id.size);
            icon = (ImageView) v.findViewById(R.id.icon);
        }
    }
}
