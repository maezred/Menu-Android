package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Backend;
import io.mdx.app.menu.model.Menu;
import io.mdx.app.menu.model.MenuSection;
import io.mdx.app.menu.viewholder.MenuItemViewHolder;
import io.mdx.app.menu.viewholder.MenuSectionViewHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class MenuFragment extends BaseFragment {
  public static final String ACTION_MENU = "io.mdx.app.menu.MENU";

  private static FragmentType TYPE = FragmentType.MENU;

  private List data = new ArrayList();

  private RecyclerViewAdapter menuAdapter;
  private RecyclerView        list;

  public MenuFragment() {
    super(TYPE, R.layout.fragment_menu);

    fetchMenu();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    createAdapter();
    setupList();
  }

  private void createAdapter() {
    menuAdapter = new RecyclerViewAdapter(getContext());
    menuAdapter.setViewHolders(MenuSectionViewHolder.class, MenuItemViewHolder.class);
    menuAdapter.changeDataSet(data);
  }

  private void fetchMenu() {
    Backend.getMenu()
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
          data = list;

          if (menuAdapter != null) {
            menuAdapter.changeDataSet(data);
          }
        }
      });
  }

  private void setupList() {
    list = (RecyclerView) getView();
    list.setLayoutManager(new LinearLayoutManager(getContext()));
    list.setAdapter(menuAdapter);
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
