package com.marc_wieser.homecut.content_provider;

import android.content.ContentProvider;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by marc on 14/10/2016.
 */

public class HomecutContract {
    public static final String CONTENT_AUTHORITY = "com.marc_wieser.homecut";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_SHORTCUT = "shortcut";

    public static class ShortcutEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHORTCUT).build();

        public static final String TABLE_NAME = "shortcut";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SUBJECT = "subject";

        public static Uri buildWithId(long id){
            return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
        }
    }
}
