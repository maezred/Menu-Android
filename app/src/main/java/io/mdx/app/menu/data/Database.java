package io.mdx.app.menu.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.mdx.app.menu.MenuApplication;
import io.mdx.app.menu.data.favorites.Favorites;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by moltendorf on 16/5/12.
 */
public class Database extends SQLiteOpenHelper {
  private static final String DB_NAME    = "menu_db";
  private static final int    DB_VERSION = 1;

  private static final Database INSTANCE = new Database();

  // While this is potentially bad for performance, we do not expect high load and want everything to execute in order.
  private static final Scheduler SCHEDULER = Schedulers.from(Executors.newSingleThreadExecutor());

  public static Database getInstance() {
    return INSTANCE;
  }

  public static Scheduler getScheduler() {
    return SCHEDULER;
  }

  static {
    // Make sure we get first dibs on the scheduler to create the database if needed.
    INSTANCE.getWritableDatabase();
  }

  private Database() {
    super(MenuApplication.getInstance().getApplicationContext(), DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Timber.d("Creating new database.");

    Favorites.createTable();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // No upgrades for now.
  }

  public static <T> Observable<T> observe(final Callable<T> callable) {
    Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {
      @Override
      public void call(Subscriber<? super T> subscriber) {
        try {
          subscriber.onNext(callable.call());
          subscriber.onCompleted();
        } catch (Exception exception) {
          Timber.e(exception, "Caught exception in database thread.");
        }
      }
    });

    return observable.subscribeOn(SCHEDULER);
  }

  public static Observable<Void> observe(final Runnable runnable) {
    Observable<Void> observable = Observable.create(new Observable.OnSubscribe<Void>() {
      @Override
      public void call(Subscriber<? super Void> subscriber) {
        try {
          runnable.run();
          subscriber.onNext(null);
          subscriber.onCompleted();
        } catch (Exception exception) {
          Timber.e(exception, "Caught exception in database thread.");
        }
      }
    });

    return observable.subscribeOn(SCHEDULER);
  }
}
