package com.marc_wieser.homecut.services;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.marc_wieser.homecut.R;
import com.marc_wieser.homecut.content_provider.HomecutContract;
import com.marc_wieser.homecut.content_provider.HomecutContract.ShortcutEntry;


public class ShareService extends IntentService {

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

        name = intent.getStringExtra(Intent.EXTRA_TITLE);
        type = intent.getType();

        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.setType(type);
        shortcutIntent.setData(stream);


        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

}
