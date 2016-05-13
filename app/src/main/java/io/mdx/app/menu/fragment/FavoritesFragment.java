package io.mdx.app.menu.fragment;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.List;
import java.util.Set;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.favorites.Favorites;
import io.mdx.app.menu.model.MenuItem;
import io.mdx.app.menu.view.ItemHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class FavoritesFragment extends RecyclerFragment {
  public static final String ACTION_FAVORITES = "io.mdx.app.menu.FAVORITES";

  private static FragmentType TYPE = FragmentType.FAVORITES;

  public FavoritesFragment() {
    super(R.layout.fragment_favorites_list);

    registerObservers();
  }

  private void registerObservers() {
    Favorites.getEventBus().observe()
      .compose(this.<Favorites.FavoritesEvent>bindUntilEvent(FragmentEvent.DESTROY))
      .subscribe(new Action1<Favorites.FavoritesEvent>() {
        @Override
        public void call(Favorites.FavoritesEvent favoritesEvent) {
          // @todo Use a scalpel and inject/replace the row directly into the view.
          fetchData();
        }
      });
  }

  @Override
  public void populateFactories(Set<RecyclerViewAdapter.Factory> factories) {
    factories.add(new ItemHolder.Factory(R.layout.row_favorites_item));
  }

  @Override
  public void fetchData() {
    Favorites.getFavorites()
      .compose(this.<List<MenuItem>>bindUntilEvent(FragmentEvent.DESTROY))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<List<MenuItem>>() {
        @Override
        public void call(List<MenuItem> data) {
          changeDataSet(data);
        }
      });
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
