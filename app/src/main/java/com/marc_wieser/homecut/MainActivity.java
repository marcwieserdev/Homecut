/*
 * Marc Wieser Copyright (c) 2016.
 * Created by Marc Wieser on 22/10/2016
 * If you have any problem or question about this work
 * please contact the author at marc.wieser33@gmail.com
 *
 * The following code is owned by Marc Wieser you can't
 * use it without any authorization or special instructions
 */
package com.marc_wieser.homecut;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.marc_wieser.homecut.services.ShareService;

import java.security.Permission;
import java.security.Permissions;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RES = 4242;
    private void logIntent(Intent intent){
        Bundle bundle = intent.getExtras();
        Set<String> categories = intent.getCategories();
        String type = intent.getType();
        String action = intent.getAction();
        if (action != null)
            Log.d("DUMP-ACTION", action);
        if (type != null)
            Log.d("DUMP-TYPE", type);
        if (intent.getDataString() != null)
            Log.d("DUMP-DATA", intent.getDataString());
        if (categories != null)
        {
            for(String key : categories){
                Log.d("DUMP-CATEGORIES", key);
            }
        }
        if (bundle != null){
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("DUMP-BUNDLE", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RES){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = getIntent();
                intent.setClass(this, ShareService.class);
                startService(intent);
                finish();
                return;
            }
            Toast.makeText(this, R.string.error_permission, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && (Intent.ACTION_SEND.equals(intent.getAction()) || Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction()))){
            if (BuildConfig.DEBUG){
                logIntent(intent);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){ //TODO : replace by constant
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, REQUEST_RES);
                    return;
                }
            }
            intent.setClass(this, ShareService.class);
            startService(intent);
            finish();
        }
        setContentView(R.layout.welcoming);
        findViewById(R.id.get_started).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }
}
