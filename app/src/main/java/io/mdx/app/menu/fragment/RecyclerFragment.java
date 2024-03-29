package io.mdx.app.menu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.mdx.app.menu.data.favorites.Favorites;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/4/29.
 */
@SuppressLint("ValidFragment")
abstract public class RecyclerFragment extends BaseFragment {
  protected RecyclerViewAdapter adapter;
  protected List data = new ArrayList();
  protected RecyclerView list;
  protected Subscription subscription;

  public RecyclerFragment(int layout) {
    super(layout);

    registerObservers();

    subscription = fetchData();
  }

  private void registerObservers() {
    Favorites.getEventBus().observe()
      .compose(this.<Favorites.FavoritesEvent>bindUntilEvent(FragmentEvent.DESTROY))
      .subscribe(new Action1<Favorites.FavoritesEvent>() {
        @Override
        public void call(Favorites.FavoritesEvent favoritesEvent) {
          if (!getUserVisibleHint()) {
            // @todo Use a scalpel and inject/replace the row directly into the view.
            if (subscription != null && !subscription.isUnsubscribed()) {
              subscription.unsubscribe();
            }

            fetchData();
          }
        }
      });
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

  abstract public Subscription fetchData();

  abstract public void populateFactories(Set<RecyclerViewAdapter.Factory> factories);
}
