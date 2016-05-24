package io.mdx.app.menu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.mdx.app.menu.R;
import io.mdx.app.menu.fragment.DetailFragment;

/**
 * Created by moltendorf on 16/5/19.
 */
public class DetailActivity extends WrapperActivity {
  private static final String BUNDLE = "BUNDLE";

  public static Intent createIntent(Context context, Bundle bundle) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(BUNDLE, bundle);

    return intent;
  }

  public DetailActivity() {
    super(R.layout.activity_detail);
  }

  @Override
  protected Bundle getBundle() {
    Intent intent = getIntent();

    if (intent != null) {
      return intent.getBundleExtra(BUNDLE);
    }

    return null;
  }

  @Override
  protected Fragment onCreateFragment() {
    return new DetailFragment();
  }
}
