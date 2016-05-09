package io.mdx.app.menu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/4/29.
 */
public class MenuFragment extends BaseFragment {
  public static final String ACTION_MENU = "io.mdx.app.menu.MENU";

  private static FragmentType TYPE = FragmentType.MENU;

  public MenuFragment() {
    this(TYPE);
  }

  @SuppressLint("ValidFragment")
  protected MenuFragment(FragmentType type) {
    super(type, R.layout.fragment_menu);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  public static class Factory implements FragmentFactory<MenuFragment> {
    @Override
    public FragmentType getType() {
      return TYPE;
    }

    @Override
    public MenuFragment newInstance() {
      return new MenuFragment();
    }
  }
}
