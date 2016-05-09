package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.R;
import io.mdx.app.menu.model.Specials;
import io.mdx.app.menu.network.Backend;
import io.mdx.app.menu.viewholder.SpecialViewHolder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    createAdapter();
    fetchSpecials();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupList();
  }

  private void createAdapter() {
    specialsAdapter = new RecyclerViewAdapter(getContext());
    specialsAdapter.setViewHolders(SpecialViewHolder.class);
  }

  private void fetchSpecials() {
    Observable<Specials> observable = Backend.getService().getSpecials();

    observable.subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Subscriber<Specials>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
          Timber.e(e.getMessage());
        }

        @Override
        public void onNext(Specials specials) {
          specialsAdapter.changeDataSet(specials.getItems());
        }
      });
  }

  private void setupList() {
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
