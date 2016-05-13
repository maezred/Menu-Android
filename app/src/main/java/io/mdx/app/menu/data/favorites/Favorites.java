package io.mdx.app.menu.data.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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

  public static Runnable createTable(final SQLiteDatabase db) {
    return new Runnable() {
      @Override
      public void run() {
        db.execSQL(SQL.CREATE_TABLE);
      }
    };
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
}
