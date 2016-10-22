package com.marc_wieser.homecut.content_provider;
/*
 * Marc Wieser Copyright (c) 2016.
 * Created by Marc Wieser on 22/10/2016
 * If you have any problem or question about this work
 * please contact the author at marc.wieser33@gmail.com
 *
 * The following code is owned by Marc Wieser you can't
 *  use it without any authorization or special instructions
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

public class HomecutProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher;

    public static final int SHORTCUT = 0;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(HomecutContract.CONTENT_AUTHORITY, HomecutContract.PATH_SHORTCUT, SHORTCUT);
    }

    private Context mContext;
    private HomecutDBHelper db;
    public HomecutProvider() {
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        db = new HomecutDBHelper(mContext);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)){
            case SHORTCUT:
                return HomecutContract.ShortcutEntry.buildWithId(db.getWritableDatabase().insert(HomecutContract.ShortcutEntry.TABLE_NAME, null, values));
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)){
            case SHORTCUT:
                return db.getReadableDatabase().query(true, HomecutContract.ShortcutEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case SHORTCUT:
                return db.getReadableDatabase().update(HomecutContract.ShortcutEntry.TABLE_NAME, values, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case SHORTCUT:
                return db.getWritableDatabase().delete(HomecutContract.ShortcutEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
