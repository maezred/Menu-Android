package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/5/4.
 */
public class SpecialFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_special, container, false);
  }

  public static class Factory implements FragmentFactory<SpecialFragment> {
    @Override
    public SpecialFragment newInstance() {
      return new SpecialFragment();
    }

    @Override
    public FragmentType getType() {
      return FragmentType.SPECIALS;
    }
  }
}
