package com.marc_wieser.homecut.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marc_wieser.homecut.content_provider.HomecutContract.ShortcutEntry;

/**
 * Created by marc on 14/10/2016.
 */

public class HomecutDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "homecut.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    public HomecutDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SHORTCUT = "CREATE TABLE IF NOT EXISTS " + ShortcutEntry.TABLE_NAME + "(" +
                ShortcutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ShortcutEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ShortcutEntry.COLUMN_URL + " TEXT NOT NULL, " +
                ShortcutEntry.COLUMN_TYPE + " TEXT, " +
                ShortcutEntry.COLUMN_SUBJECT + " TEXT, " +
                "UNIQUE(" + ShortcutEntry.COLUMN_URL + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_SHORTCUT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mContext.deleteDatabase(DATABASE_NAME);
        onCreate(db);
    }
}
