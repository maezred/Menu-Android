package io.mdx.app.menu.data.favorites;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.mdx.app.menu.data.Bus;
import io.mdx.app.menu.data.Database;
import io.mdx.app.menu.model.MenuItem;
import rx.Observable;
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
        }
      })
      .subscribe();
  }

  public static Observable<List<MenuItem>> getFavorites() {
    return Database
      .observe(new Callable<Cursor>() {
        @Override
        public Cursor call() throws Exception {
          return Database.getInstance().getReadableDatabase()
            .rawQuery(SQL.GET_FAVORITES, NONE);
        }
      })
      .observeOn(Schedulers.computation())
      .map(new Func1<Cursor, List<MenuItem>>() {
        @Override
        public List<MenuItem> call(Cursor cursor) {
          List<MenuItem> items = new ArrayList<>(cursor.getCount());
          cursor.moveToFirst();

          while (!cursor.isAfterLast()) {
            items.add(new MenuItem(cursor));
            cursor.moveToNext();
          }

          cursor.close();

          Timber.d("Found %d favorites.", items.size());

          return items;
        }
      });
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
      .subscribe();
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
      .subscribe();
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
