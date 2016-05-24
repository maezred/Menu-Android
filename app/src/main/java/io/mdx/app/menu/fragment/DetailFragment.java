package io.mdx.app.menu.fragment;

import android.os.Bundle;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/5/19.
 */
public class DetailFragment extends BaseFragment {
  private static final String ITEM_ID = "ITEM_ID";

  public static Bundle createBundle(int itemId) {
    Bundle bundle = new Bundle();
    bundle.putInt(ITEM_ID, itemId);

    return bundle;
  }

  public DetailFragment() {
    super(R.layout.fragment_detail);
  }
}
