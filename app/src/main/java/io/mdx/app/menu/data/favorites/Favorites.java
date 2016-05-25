package io.mdx.app.menu.data.favorites;

import android.database.Cursor;

import java.util.concurrent.Callable;

import io.mdx.app.menu.data.Bus;
import io.mdx.app.menu.data.Cache;
import io.mdx.app.menu.data.CanonicalSet;
import io.mdx.app.menu.data.Database;
import io.mdx.app.menu.model.MenuItem;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by moltendorf on 16/5/12.
 */
public class Favorites {
  private static final String[] NONE = new String[0];

  public static final String TABLE = "favorites";

  public static final String C_NAME        = "name";
  public static final String C_PRICE       = "price";
  public static final String C_DESCRIPTION = "description";
  public static final String C_PICTURE     = "picture";

  private static Observable<CanonicalSet<MenuItem>> favorites;

  private static Bus<FavoritesEvent> eventBus = new Bus<>();

  public static Bus<FavoritesEvent> getEventBus() {
    return eventBus;
  }

  public static void createTable() {
    Database
      .observe(new Runnable() {
        @Override
        public void run() {
          Database.getInstance().getWritableDatabase()
            .execSQL(SQL.CREATE_TABLE);

          Timber.d("Favorites table created.");
        }
      })
      .subscribe();
  }

  public static Observable<CanonicalSet<MenuItem>> getFavorites() {
    if (favorites == null) {
      favorites = Database
        .observe(new Callable<Cursor>() {
          @Override
          public Cursor call() throws Exception {
            return Database.getInstance().getReadableDatabase()
              .rawQuery(SQL.GET_FAVORITES, NONE);
          }
        })
        .observeOn(Schedulers.computation())
        .map(new Func1<Cursor, CanonicalSet<MenuItem>>() {
          @Override
          public CanonicalSet<MenuItem> call(Cursor cursor) {
            CanonicalSet<MenuItem> items = new CanonicalSet<>(cursor.getCount());
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
              items.add(new MenuItem(cursor));
              cursor.moveToNext();
            }

            cursor.close();

            Cache.addOrUpdateItems(items, true);
            Timber.d("Found %d favorites.", items.size());

            return items;
          }
        })
        .cache();
    }

    return favorites;
  }

  public static Observable<Boolean> isFavorite(final String name) {
    return Database
      .observe(new Callable<Cursor>() {
        @Override
        public Cursor call() throws Exception {
          return Database.getInstance().getReadableDatabase()
            .rawQuery(SQL.IS_FAVORITE, new String[]{name});
        }
      })
      .observeOn(Schedulers.computation())
      .map(new Func1<Cursor, Boolean>() {
        @Override
        public Boolean call(Cursor cursor) {
          Boolean isFavorite = cursor.getCount() > 0;
          cursor.close();

          return isFavorite;
        }
      });
  }

  public static void addFavorite(final MenuItem item) {
    Database
      .observe(new Runnable() {
        @Override
        public void run() {
          Timber.d("Added favorite to database: %s.", item.getName());
          Database.getInstance().getWritableDatabase()
            .execSQL(SQL.ADD_FAVORITE, new String[]{
              item.getName(),
              item.getPrice(),
              item.getDescription(),
              item.getPicture()
            });
        }
      })
      .observeOn(Database.getScheduler())
      .subscribe(new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
          synchronized (Favorites.class) {
            favorites = null; // @todo Modify set instead of nulling it.
          }

          eventBus.send(new FavoritesEvent(FavoritesEvent.Type.ADD, item));
        }
      });
  }

  public static void removeFavorite(final MenuItem item) {
    Database
      .observe(new Runnable() {
        @Override
        public void run() {
          Timber.d("Removed favorite from database: %s.", item.getName());
          Database.getInstance().getWritableDatabase()
            .execSQL(SQL.REMOVE_FAVORITE, new String[]{item.getName()});
        }
      })
      .subscribe(new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
          synchronized (Favorites.class) {
            favorites = null; // @todo Modify set instead of nulling it.
          }

          eventBus.send(new FavoritesEvent(FavoritesEvent.Type.REMOVE, item));
        }
      });
  }

  public static void updateFavorite(final MenuItem item) {
    Database
      .observe(new Runnable() {
        @Override
        public void run() {
          Timber.d("Updated favorite in database: %s.", item.getName());
          Database.getInstance().getWritableDatabase()
            .execSQL(SQL.UPDATE_FAVORITE, new String[]{
              item.getPrice(),
              item.getDescription(),
              item.getPicture(),

              item.getName() // WHERE clause
            });
        }
      })
      .subscribe(new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
          synchronized (Favorites.class) {
            favorites = null; // @todo Modify set instead of nulling it.
          }

          eventBus.send(new FavoritesEvent(FavoritesEvent.Type.UPDATE, item));
        }
      });
  }

  public static class FavoritesEvent {
    private Type     type;
    private MenuItem item;

    public FavoritesEvent(Type type, MenuItem item) {
      this.type = type;
      this.item = item;
    }

    public Type getType() {
      return type;
    }

    public MenuItem getItem() {
      return item;
    }

    public enum Type {
      ADD,
      REMOVE,
      UPDATE
    }
  }
}
