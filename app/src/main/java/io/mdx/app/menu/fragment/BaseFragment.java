package io.mdx.app.menu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by moltendorf on 16/4/29.
 */
@SuppressLint("ValidFragment")
public class BaseFragment extends RxFragment {
  protected FragmentType type;
  private   int          layout;

  protected BaseFragment(FragmentType type, int layout) {
    this.type = type;
    this.layout = layout;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layout, container, false);
  }
}
