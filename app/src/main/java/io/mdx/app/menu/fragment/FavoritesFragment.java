package io.mdx.app.menu.fragment;

import android.content.Intent;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.mdx.app.menu.R;
import io.mdx.app.menu.activity.DetailActivity;
import io.mdx.app.menu.data.favorites.Favorites;
import io.mdx.app.menu.model.MenuItem;
import io.mdx.app.menu.view.ItemHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class FavoritesFragment extends RecyclerFragment {
  public static final String ACTION_FAVORITES = "io.mdx.app.menu.FAVORITES";

  private static FragmentType TYPE = FragmentType.FAVORITES;

  public FavoritesFragment() {
    super(R.layout.fragment_favorites_list);
  }

  @Override
  public void populateFactories(Set<RecyclerViewAdapter.Factory> factories) {
    ItemHolder.Factory factory = new ItemHolder.Factory(R.layout.row_favorites_item);

    factory.created()
      .compose(this.<ItemHolder>bindUntilEvent(FragmentEvent.DESTROY))
      .subscribe(new Action1<ItemHolder>() {
        @Override
        public void call(ItemHolder itemHolder) {
          RxView.clicks(itemHolder.itemView)
            .compose(FavoritesFragment.this.<Void>bindUntilEvent(FragmentEvent.DESTROY))
            .subscribe(new Action1<Void>() {
              @Override
              public void call(Void aVoid) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);
              }
            });
        }
      });

    factories.add(factory);
  }

  @Override
  public Subscription fetchData() {
    return Favorites.getFavorites()
      .compose(this.<Set<MenuItem>>bindUntilEvent(FragmentEvent.DESTROY))
      .map(new Func1<Set<MenuItem>, List<MenuItem>>() {
        @Override
        public List<MenuItem> call(Set<MenuItem> data) {
          return new ArrayList<>(data);
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<List<MenuItem>>() {
        @Override
        public void call(List<MenuItem> data) {
          changeDataSet(data);
        }
      });
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if (!isVisibleToUser) {
      if (subscription != null && !subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }

      fetchData();
    }
  }

  public static class Factory implements FragmentFactory<FavoritesFragment> {
    @Override
    public FragmentType getType() {
      return TYPE;
    }

    @Override
    public FavoritesFragment newInstance() {
      return new FavoritesFragment();
    }
  }
}
