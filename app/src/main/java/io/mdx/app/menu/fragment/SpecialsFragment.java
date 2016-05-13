package io.mdx.app.menu.fragment;

import android.view.MotionEvent;
import android.view.View;

import com.trello.rxlifecycle.FragmentEvent;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import java.util.Set;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Backend;
import io.mdx.app.menu.model.Specials;
import io.mdx.app.menu.view.ItemHolder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/4/29.
 */
public class SpecialsFragment extends RecyclerFragment {
  public static final String ACTION_SPECIALS = "io.mdx.app.menu.SPECIALS";

  private static FragmentType TYPE = FragmentType.SPECIALS;

  public SpecialsFragment() {
    super(R.layout.fragment_specials_list);
  }

  @Override
  public void populateFactories(Set<RecyclerViewAdapter.Factory> factories) {
    factories.add(new ItemHolder.Factory(R.layout.row_special_item));
  }

  @Override
  public Subscription fetchData() {
    return Backend.getSpecials()
      .compose(this.<Specials>bindUntilEvent(FragmentEvent.DESTROY))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Specials>() {
        @Override
        public void call(Specials specials) {
          changeDataSet(specials.getItems());
        }
      });
  }

  @Override
  public void setupList() {
    super.setupList();

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
