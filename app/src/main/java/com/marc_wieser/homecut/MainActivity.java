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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.marc_wieser.homecut.services.ShareService;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private void logIntent(Intent intent){
        Bundle bundle = intent.getExtras();
        Set<String> categories = intent.getCategories();
        String type = intent.getType();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_SEND.equals(intent.getAction())){
            logIntent(intent);
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
