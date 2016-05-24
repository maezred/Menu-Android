package io.mdx.app.menu;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by moltendorf on 16/5/4.
 */
public class MenuApplication extends Application {
  private static MenuApplication instance;

  public static MenuApplication getInstance() {
    return instance;
  }

  public static String getAction(String action) {
    return String.format("%s.%s", instance.getPackageName(), action);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }
}
