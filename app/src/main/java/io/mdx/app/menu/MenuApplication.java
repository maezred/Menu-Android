package io.mdx.app.menu;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by moltendorf on 16/5/4.
 */
public class MenuApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }
}
