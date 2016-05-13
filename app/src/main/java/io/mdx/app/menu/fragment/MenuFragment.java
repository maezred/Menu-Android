package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Backend;
import io.mdx.app.menu.model.Menu;
import io.mdx.app.menu.model.MenuSection;
import io.mdx.app.menu.view.ItemHolder;
import io.mdx.app.menu.view.SectionHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class MenuFragment extends RecyclerFragment {
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
    ItemHolder.Factory    itemFactory    = new ItemHolder.Factory(R.layout.row_menu_item);
    SectionHolder.Factory sectionFactory = new SectionHolder.Factory(R.layout.row_menu_section);

    Set<RecyclerViewAdapter.Factory> factories = new LinkedHashSet<>();
    factories.add(itemFactory);
    factories.add(sectionFactory);

    menuAdapter = new RecyclerViewAdapter(getContext());
    menuAdapter.setViewHolders(factories);
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
