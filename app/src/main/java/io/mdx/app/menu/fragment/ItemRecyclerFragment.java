package io.mdx.app.menu.fragment;

import android.content.Intent;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;

import io.mdx.app.menu.MenuApplication;
import io.mdx.app.menu.activity.DetailActivity;
import io.mdx.app.menu.view.ItemHolder;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/5/22.
 */
abstract public class ItemRecyclerFragment extends RecyclerFragment {
  public ItemRecyclerFragment(int layout) {
    super(layout);
  }

  protected void registerItemHolderFactory(ItemHolder.Factory factory) {
    if (MenuApplication.actionEnabled(DetailFragment.ACTION_DETAIL)) {
      factory.created()
        .compose(this.<ItemHolder>bindUntilEvent(FragmentEvent.DESTROY))
        .subscribe(new Action1<ItemHolder>() {
          @Override
          public void call(ItemHolder itemHolder) {
            RxView.clicks(itemHolder.itemView)
              .compose(ItemRecyclerFragment.this.<Void>bindUntilEvent(FragmentEvent.DESTROY))
              .subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                  Intent intent = new Intent(getContext(), DetailActivity.class);
                  startActivity(intent);
                }
              });
          }
        });
    }
  }
}
