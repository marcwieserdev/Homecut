/*
 * Marc Wieser Copyright (c) 2016.
 * Created by Marc Wieser on 22/10/2016
 * If you have any problem or question about this work
 * please contact the author at marc.wieser33@gmail.com
 *
 * The following code is owned by Marc Wieser you can't
 * use it without any authorization or special instructions
 */
package com.marc_wieser.homecut.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.marc_wieser.homecut.BuildConfig;
import com.marc_wieser.homecut.R;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


public class ShareService extends IntentService {
    private final String LOG_TAG = getClass().getSimpleName();
    public ShareService() {
        super("ShareService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null)
            return;
        switch (intent.getAction()){
            case Intent.ACTION_SEND:
                handleActionSend(intent);
                break;
            case Intent.ACTION_SEND_MULTIPLE:
                handleActionSendMultiple(intent);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported action");
        }
    }

    private void handleActionSendMultiple(Intent intent){
        ArrayList<Uri> streams = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (streams == null)
            return;
        for (Uri stream : streams) {
            if (stream == null){
                if (BuildConfig.DEBUG)
                    Log.e(LOG_TAG, "Stream null");
                continue;
            }
            String name = getShortcutName(stream);
            if (name == null){
                if (BuildConfig.DEBUG)
                    Log.i(LOG_TAG, "Unsupported device, stream Uri : " + stream);
                continue;
            }

            String type = getShortcutType(stream);
            if (BuildConfig.DEBUG)
                Log.d(LOG_TAG, type);
            int resourceID = getShortcutIcon(type);
            createShortcut(name, resourceID, stream, type);
        }

    }

    private int getShortcutIcon(String type){
        int resourceID;
        if (type.startsWith("pdf") || type.endsWith("pdf")){
            resourceID = R.drawable.ic_pdf;
        } else if (type.startsWith("image") || type.endsWith("image")){
            resourceID = R.drawable.ic_photo;
        } else if (type.startsWith("video") || type.endsWith("video")) {
            resourceID = R.drawable.ic_movie;
        } else if (type.startsWith("audio") || type.endsWith("audio")){
            resourceID = R.drawable.ic_audio;
        } else {
            resourceID = R.drawable.ic_file;
        }
        return resourceID;
    }

    @Nullable
    private String getShortcutName(Uri stream){
        final String[] projection = {
                OpenableColumns.DISPLAY_NAME
        };
        String name = null;
        if (stream.toString().startsWith("content:")){
            Cursor file = getContentResolver().query(stream, projection, null, null, null);
            if (file == null || !file.moveToFirst()){
                if (BuildConfig.DEBUG)
                    Log.e(LOG_TAG, "file resolving failed");
                return null;
            }
            name = file.getString(0);
            file.close();
        } else if (stream.toString().startsWith("file:")){
            File file = new File(URI.create(stream.toString()));
            name = file.getName();
        }
        return name;
    }

    private String getShortcutType(Uri stream){
        String type = null;

        if (stream.toString().startsWith("content:")){
            type = getContentResolver().getType(stream);
        } else if (stream.toString().startsWith("file:")){
            type = MimeTypeMap.getFileExtensionFromUrl(stream.toString());
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(type);
        }
        return type;
    }

    @Nullable
    private Uri getContentUri(String filePath, String mime){
        if (mime.startsWith("image") || mime.endsWith("image")){
            Cursor file = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA+"=?", new String[]{filePath}, null);
            if (file != null && file.moveToFirst()){
                long id = file.getLong(0);
                file.close();
                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        } else if (mime.startsWith("video") || mime.endsWith("video")){
            Cursor file = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA+"=?", new String[]{filePath}, null);
            if (file != null && file.moveToFirst()){
                long id = file.getLong(0);
                file.close();
                return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        } else if (mime.startsWith("audio") || mime.endsWith("audio")){
            Cursor file = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media._ID}, MediaStore.Audio.Media.DATA+"=?", new String[]{filePath}, null);
            if (file != null && file.moveToFirst()){
                long id = file.getLong(0);
                file.close();
                return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
            }
        }
        return null;
    }

    private void createShortcut(String name, int resourceID, Uri stream, String type){
        Intent shortcutIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            shortcutIntent.setAction(Intent.ACTION_QUICK_VIEW);
        } else {
            shortcutIntent.setAction(Intent.ACTION_VIEW);
        }
        shortcutIntent.setType(type);
        if (stream.toString().startsWith("file:")){
            File file = new File(URI.create(stream.toString()));
            Uri newStream = getContentUri(file.getAbsolutePath(), type);
            if (newStream != null)
                stream = newStream;
        }
        shortcutIntent.setData(stream);
        if (BuildConfig.DEBUG)
            Log.d(LOG_TAG, String.valueOf(stream));
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        resourceID));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    private void handleActionSend(Intent intent){
        String name, type;
        Uri stream = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (stream == null){
            if (BuildConfig.DEBUG)
                Log.e(LOG_TAG, "Stream null");
            return;
        }

        name = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (name == null || name.isEmpty()){
           name = getShortcutName(stream);
            if (name == null || name.isEmpty()){
                if (BuildConfig.DEBUG)
                    Log.i(LOG_TAG, "Unsupported device, stream Uri : " + stream);
                return;
            }
        }
        type = intent.getType();
        if (type == null || type.isEmpty())
            type = getShortcutType(stream);
        int resourceID = getShortcutIcon(type);
        createShortcut(name, resourceID, stream, type);
        if (BuildConfig.DEBUG)
            Log.i(LOG_TAG, "Shortcut creation success");
    }

}
