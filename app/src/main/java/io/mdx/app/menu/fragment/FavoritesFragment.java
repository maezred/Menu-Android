package io.mdx.app.menu.fragment;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.mdx.app.menu.Action;
import io.mdx.app.menu.R;
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
public class FavoritesFragment extends ItemRecyclerFragment {
  public static final Action ACTION_FAVORITES = new Action("FAVORITES");

  private static Factory<FavoritesFragment> factory = new Factory<FavoritesFragment>() {
    @Override
    public Action getAction() {
      return ACTION_FAVORITES;
    }

    @Override
    public FavoritesFragment newInstance() {
      return FavoritesFragment.newInstance();
    }
  };

  public static Factory<FavoritesFragment> getFactory() {
    return factory;
  }

  public static FavoritesFragment newInstance() {
    return new FavoritesFragment();
  }

  public FavoritesFragment() {
    super(R.layout.fragment_favorites_list);
  }

  @Override
  public void populateFactories(Set<RecyclerViewAdapter.Factory> factories) {
    ItemHolder.Factory factory = new ItemHolder.Factory(R.layout.row_favorites_item);

    registerItemHolderFactory(factory);

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
}
