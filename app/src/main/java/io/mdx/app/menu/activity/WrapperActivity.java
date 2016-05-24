package io.mdx.app.menu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/5/19.
 */
abstract public class WrapperActivity extends BaseActivity {
  private FrameLayout container;

  public WrapperActivity(int layout) {
    super(layout);
  }

  @Override
  protected void onViewCreated() {
    super.onViewCreated();

    container = (FrameLayout) findViewById(R.id.container);

    setupFragment();
  }

  protected Bundle getBundle() {
    return null;
  }

  protected void setupFragment() {
    Fragment fragment = onCreateFragment();
    Bundle   bundle   = getBundle();

    if (bundle != null) {
      fragment.setArguments(bundle);
    }

    getSupportFragmentManager()
      .beginTransaction()
      .replace(R.id.container, fragment)
      .commit();
  }

  abstract protected Fragment onCreateFragment();
}
