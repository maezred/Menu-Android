package io.mdx.app.menu.fragment;

import io.mdx.app.menu.MenuApplication;
import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/5/19.
 */
public class DetailFragment extends BaseFragment {
  public static final String ACTION_DETAIL = MenuApplication.getAction("DETAIL");

  public DetailFragment() {
    super(R.layout.fragment_detail);
  }
}
