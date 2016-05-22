package io.mdx.app.menu.data;

import java.util.List;
import java.util.ListIterator;

import io.mdx.app.menu.model.MenuItem;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by moltendorf on 16/5/21.
 */
public class Cache {
  private static final CanonicalSet<MenuItem> items = new CanonicalSet<>();

  private static Bus<MenuItem> updatedItemBus = new Bus<>();

  public static Observable<MenuItem> getItem(final Object item) {
    return Observable
      .create(new Observable.OnSubscribe<MenuItem>() {
        @Override
        public void call(Subscriber<? super MenuItem> subscriber) {
          MenuItem existing;

          synchronized (items) {
            existing = items.get(item);
          }

          subscriber.onNext(existing);
        }
      })
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.immediate());
  }

  public static MenuItem addOrUpdateItem(MenuItem item, boolean setFavorite) {
    MenuItem existing = null;

    synchronized (items) {
      if (items.add(item)) {
        return item;
      } else {
        existing = items.get(item);
      }
    }

    if (existing != null) {
      check:
      {
        if (!existing.getName().equals(item.getName())) {
          break check;
        }

        if (!existing.getPrice().equals(item.getPrice())) {
          break check;
        }

        if (!existing.getDescription().equals(item.getDescription())) {
          break check;
        }

        if (!existing.getPicture().equals(item.getPicture())) {
          break check;
        }

        if (!existing.getDisplay().equals(item.getDisplay())) {
          break check;
        }

        return existing;
      }

      if (!setFavorite) {
        item.setFavorite(existing.getFavorite());
      }

      synchronized (items) {
        items.update(item);
      }

      updatedItemBus.send(item);
    }

    return item;
  }

  public static void addOrUpdateItems(List<MenuItem> items, boolean setFavorite) {
    ListIterator<MenuItem> iterator = items.listIterator();

    while (iterator.hasNext()) {
      iterator.set(addOrUpdateItem(iterator.next(), setFavorite));
    }
  }

  public static Bus<MenuItem> getUpdatedItemBus() {
    return updatedItemBus;
  }
}
