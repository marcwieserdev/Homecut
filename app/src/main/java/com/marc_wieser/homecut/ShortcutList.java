/*
 * Marc Wieser Copyright (c) 2016.
 * Created by Marc Wieser on 22/10/2016
 * If you have any problem or question about this work
 * please contact the author at marc.wieser33@gmail.com
 *
 * The following code is owned by Marc Wieser you can't
 *  use it without any authorization or special instructions
 */
package com.marc_wieser.homecut;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marc_wieser.homecut.content_provider.HomecutContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShortcutList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static String TAG = ShortcutList.class.getName();
    private final static String LOG_TAG = ShortcutList.class.getSimpleName();
    private final static int LOADER_SHORTCUT = 0;
    private ShortcutListAdapter mAdapter;

    private ListView mShortcutList;

    public ShortcutList() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_SHORTCUT, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shortcut_list, container, false);
        mShortcutList = (ListView) v.findViewById(R.id.shortcut_list);
        mAdapter = new ShortcutListAdapter(getActivity(), null, 0);
        mShortcutList.setAdapter(mAdapter);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_SHORTCUT){
            return new CursorLoader(getActivity(), HomecutContract.ShortcutEntry.CONTENT_URI, null, null, null, null);
        }
        throw new IllegalArgumentException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if (id == LOADER_SHORTCUT){
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id == LOADER_SHORTCUT){
            mAdapter.swapCursor(null);
        }
    }
}
