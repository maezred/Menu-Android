package io.mdx.app.menu.data.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.mdx.app.menu.data.Bus;
import io.mdx.app.menu.model.MenuItem;
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

  public static Runnable createTable(final SQLiteDatabase db) {
    return new Runnable() {
      @Override
      public void run() {
        db.execSQL(SQL.CREATE_TABLE);
      }
    };
  }

  public static Callable<List<MenuItem>> getFavorites(final SQLiteDatabase db) {
    return new Callable<List<MenuItem>>() {
      @Override
      public List<MenuItem> call() throws Exception {
        Cursor cursor = db.rawQuery(SQL.GET_FAVORITES, NONE);

        // @todo Move all this to computation scheduler.
        List<MenuItem> items = new ArrayList<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
          items.add(new MenuItem(cursor));

          cursor.moveToNext();
        }

        Timber.d("Found %d favorites.", items.size());

        cursor.close();

        return items;
      }
    };
  }

  public static Callable<Boolean> isFavorite(final SQLiteDatabase db, final String name) {
    return new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        Cursor  cursor     = db.rawQuery(SQL.IS_FAVORITE, new String[]{name});
        Boolean isFavorite = cursor.getCount() > 0;
        cursor.close();

        return isFavorite;
      }
    };
  }

  public static Runnable addFavorite(final SQLiteDatabase db, final MenuItem item) {
    return new Runnable() {
      @Override
      public void run() {
        Timber.d("Added favorite to database: %s.", item.getName());
        db.execSQL(SQL.ADD_FAVORITE, new String[]{
          item.getName(),
          item.getPrice(),
          item.getDescription(),
          item.getPicture()
        });
      }
    };
  }

  public static Runnable removeFavorite(final SQLiteDatabase db, final MenuItem item) {
    return new Runnable() {
      @Override
      public void run() {
        Timber.d("Removed favorite from database: %s.", item.getName());
        db.execSQL(SQL.REMOVE_FAVORITE, new String[]{item.getName()});
      }
    };
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
