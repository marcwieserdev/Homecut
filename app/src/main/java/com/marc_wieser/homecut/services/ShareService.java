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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.marc_wieser.homecut.R;


public class ShareService extends IntentService {
    public final static String ACTION_CREATE_SHARING = "create_share";

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
            default:
                throw new UnsupportedOperationException("Unsupported action");
        }
    }

    private void handleActionSend(Intent intent){
        String name, type;
        Uri stream = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String[] projection = {
                OpenableColumns.DISPLAY_NAME
        };
        if (stream == null){
            //TODO : put error message
            return;
        }
        Cursor file = getContentResolver().query(stream, projection, null, null, null);
        if (file == null || !file.moveToFirst()){
            //TODO : put error message
            return;
        }

        name = intent.getStringExtra(Intent.EXTRA_TITLE);
        name = name == null || name.isEmpty() ? file.getString(0) : name;
        file.close();
        type = intent.getType();

        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.setType(type);
        shortcutIntent.setData(stream);

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

        Intent chooser = Intent.createChooser(shortcutIntent, "Open With");
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, chooser);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        resourceID));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

}
