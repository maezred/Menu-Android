package io.mdx.app.menu.activity;

import android.support.v4.app.Fragment;

import io.mdx.app.menu.R;
import io.mdx.app.menu.fragment.DetailFragment;

/**
 * Created by moltendorf on 16/5/19.
 */
public class DetailActivity extends WrapperActivity {
  public DetailActivity() {
    super(R.layout.activity_detail);
  }

  @Override
  protected Fragment onCreateFragment() {
    return new DetailFragment();
  }
}
