package io.mdx.app.menu;

import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by moltendorf on 16/5/24.
 */
public class Action {
  private static final String packageName = MenuApplication.getInstance().getPackageName();

  private boolean enabled;

  private Intent intent;

  private String action;
  private String local;

  public Action(String local) {
    this.local = local;

    action = String.format("%s.%s", packageName, local);
    intent = new Intent(action);

    enabled = MenuApplication.getInstance()
      .getPackageManager()
      .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
      .size() > 0;
  }

  public Intent getIntent() {
    return intent.cloneFilter();
  }

  public String getAction() {
    return action;
  }

  public String getLocal() {
    return local;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public String toString() {
    return action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Action action = (Action) o;

    return local.equals(action.local);
  }

  @Override
  public int hashCode() {
    return local.hashCode();
  }
}
