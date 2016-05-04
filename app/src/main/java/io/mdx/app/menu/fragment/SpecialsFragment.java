package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import io.mdx.app.menu.R;
import io.mdx.app.menu.RecyclerViewAdapter;
import io.mdx.app.menu.model.Special;
import io.mdx.app.menu.viewholder.SpecialViewHolder;

/**
 * Created by moltendorf on 16/4/29.
 */
public class SpecialsFragment extends BaseFragment {
  public static final String ACTION_SPECIALS = "io.mdx.app.menu.SPECIALS";

  private static FragmentType TYPE = FragmentType.SPECIALS;

  private RecyclerViewAdapter specialsAdapter;
  private RecyclerView        list;

  public SpecialsFragment() {
    super(TYPE, R.layout.fragment_specials_list);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupList();
  }

  private void setupList() {
    Map<Class<?>, RecyclerViewAdapter.Factory> factories = new HashMap<Class<?>, RecyclerViewAdapter.Factory>() {{
      put(Special.class, new SpecialViewHolder.Factory());
    }};

    specialsAdapter = new RecyclerViewAdapter(getContext(), factories);

    list = (RecyclerView) getView();
    list.setLayoutManager(new LinearLayoutManager(getContext()));
    list.setAdapter(specialsAdapter);
  }

  public static class Factory implements FragmentFactory<SpecialsFragment> {
    @Override
    public FragmentType getType() {
      return TYPE;
    }

    @Override
    public SpecialsFragment newInstance() {
      return new SpecialsFragment();
    }
  }
}
