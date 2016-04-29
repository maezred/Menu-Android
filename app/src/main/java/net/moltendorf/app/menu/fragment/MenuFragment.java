package net.moltendorf.app.menu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.moltendorf.app.menu.R;

/**
 * Created by moltendorf on 16/4/29.
 */
public class MenuFragment extends BaseFragment {
  private static FragmentType TYPE = FragmentType.MENU;

  public MenuFragment() {
    this(TYPE);
  }

  @SuppressLint("ValidFragment")
  protected MenuFragment(FragmentType type) {
    super(type, R.layout.fragment_main);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    TextView textView = (TextView) view.findViewById(R.id.section_label);
    textView.setText(type.getPageTitle());

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
