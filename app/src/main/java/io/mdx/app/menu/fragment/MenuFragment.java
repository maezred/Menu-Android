package io.mdx.app.menu.fragment;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.mdx.app.menu.MenuApplication;
import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Backend;
import io.mdx.app.menu.model.Menu;
import io.mdx.app.menu.model.MenuSection;
import io.mdx.app.menu.view.ItemHolder;
import io.mdx.app.menu.view.SectionHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class MenuFragment extends ItemRecyclerFragment {
  public static final String ACTION_MENU = MenuApplication.getAction("MENU");

  private static FragmentType TYPE = FragmentType.MENU;

  public MenuFragment() {
    super(R.layout.fragment_menu);
  }

  @Override
  public void populateFactories(Set<RecyclerViewAdapter.Factory> factories) {
    ItemHolder.Factory factory = new ItemHolder.Factory(R.layout.row_menu_item);

    registerItemHolderFactory(factory);

    factories.add(factory);
    factories.add(new SectionHolder.Factory(R.layout.row_menu_section));
  }

  @Override
  public Subscription fetchData() {
    return Backend.getMenu()
      .compose(this.<Menu>bindUntilEvent(FragmentEvent.DESTROY))
      .map(new Func1<Menu, List>() {
        @Override
        public List call(Menu menu) {
          List list = new ArrayList();

          for (MenuSection section : menu.getSections()) {
            list.add(section);
            list.addAll(section.getItems());
          }

          return list;
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<List>() {
        @Override
        public void call(List list) {
          changeDataSet(list);
        }
      });
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
