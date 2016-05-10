package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

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

  List data = new ArrayList();

  private RecyclerViewAdapter specialsAdapter;
  private RecyclerView        list;

  public SpecialsFragment() {
    super(TYPE, R.layout.fragment_specials_list);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fetchSpecials();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    createAdapter();
    setupList();
  }

  private void createAdapter() {
    specialsAdapter = new RecyclerViewAdapter(getContext());
    specialsAdapter.setViewHolders(SpecialViewHolder.class);
    specialsAdapter.changeDataSet(data);
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
          data = specials.getItems();

          if (specialsAdapter != null) {
            specialsAdapter.changeDataSet(data);
          }
        }
      });
  }

  private void setupList() {
    list = (RecyclerView) getView();
    list.setLayoutManager(new LinearLayoutManager(getContext()));
    list.setAdapter(specialsAdapter);

    list.setOnTouchListener(new View.OnTouchListener() {
      private int position = 0;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_UP:
            int current = list.computeVerticalScrollOffset();
            int previous = position * list.getHeight();

            // Very simple, does not use velocity. May feel buggy if the user scrolls one direction then goes the other.
            if (current > previous) {
              list.smoothScrollToPosition(++position);
            } else if (current < previous) {
              list.smoothScrollToPosition(--position);
            }
        }

        return false;
      }
    });
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
