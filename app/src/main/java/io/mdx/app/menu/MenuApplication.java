package io.mdx.app.menu;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

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

  public static boolean actionEnabled(String action) {
    Intent intent = new Intent(action);

    return instance.getPackageManager()
      .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
      .size() > 0;
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
