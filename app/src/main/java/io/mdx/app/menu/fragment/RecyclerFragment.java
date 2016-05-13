package io.mdx.app.menu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by moltendorf on 16/4/29.
 */
@SuppressLint("ValidFragment")
abstract public class RecyclerFragment extends RxFragment {
  protected RecyclerViewAdapter adapter;
  protected List data = new ArrayList();
  protected RecyclerView list;

  private int layout;

  protected RecyclerFragment(int layout) {
    this.layout = layout;

    fetchData();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layout, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    createAdapter();
    setupList();
  }

  public void changeDataSet(List data) {
    this.data = data;

    if (adapter != null) {
      adapter.changeDataSet(data);
    }
  }

  public void createAdapter() {
    Set<RecyclerViewAdapter.Factory> factories = new LinkedHashSet<>();
    populateFactories(factories);

    adapter = new RecyclerViewAdapter(getContext());
    adapter.setViewHolders(factories);
    adapter.changeDataSet(data);
  }

  public void setupList() {
    list = (RecyclerView) getView();
    list.setLayoutManager(new LinearLayoutManager(getContext()));
    list.setAdapter(adapter);
  }

  abstract public void fetchData();

  abstract public void populateFactories(Set<RecyclerViewAdapter.Factory> factories);
}
