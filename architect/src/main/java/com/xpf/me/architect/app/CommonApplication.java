package com.xpf.me.architect.app;

import android.app.Application;

/**
 * Created by xgo on 11/15/15.
 */
public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppData.init(getApplicationContext());
    }
}
