package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import io.mdx.app.menu.Action;

/**
 * Created by moltendorf on 16/5/19.
 */
abstract public class BaseFragment extends RxFragment {
  private int layout;

  protected BaseFragment(int layout) {
    this.layout = layout;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layout, container, false);
  }

  public interface Factory<T extends Fragment> {
    Action getAction();

    T newInstance();
  }
}
