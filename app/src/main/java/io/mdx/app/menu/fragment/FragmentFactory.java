package io.mdx.app.menu.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by moltendorf on 16/4/29.
 */
public interface FragmentFactory<T extends Fragment> {
  T newInstance();

  FragmentType getType();
}
