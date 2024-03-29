package io.mdx.app.menu.data;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.List;

import io.mdx.app.menu.data.favorites.Favorites;
import io.mdx.app.menu.model.Menu;
import io.mdx.app.menu.model.MenuItem;
import io.mdx.app.menu.model.MenuSection;
import io.mdx.app.menu.model.Specials;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by moltendorf on 16/5/9.
 */
abstract public class Backend {
  private static Gson gson = new GsonBuilder()
    .setFieldNamingStrategy(new FieldNamingStrategy() {
      @Override
      public String translateName(Field field) {
        return field.getName().substring(1);
      }
    }).create();

  private static Service service = (new Retrofit.Builder())
    .baseUrl("https://mexxis.mdx.co/data/")
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create(Service.class);

  private static Observable<Specials> specials;
  private static Observable<Menu>     menu;

  /**
   * Creates an observable that, when subscribed to, fetches both the current specials and the user's favorites, then uses both
   * data sets to create a list of specials with each special that's in the user's favorites list properly flagged as a
   * favorite.
   *
   * @return Observable to fetch Specials
   */
  public static Observable<Specials> getSpecials() {
    if (specials == null) {
      specials = service.getSpecials()
        .subscribeOn(Schedulers.io()) // Use IO threads for network request.
        .observeOn(Schedulers.computation()) // Use computation threads for processing data.
        .zipWith(Favorites.getFavorites(), new Func2<Specials, CanonicalSet<MenuItem>, Specials>() {
          @Override
          public Specials call(Specials specials, CanonicalSet<MenuItem> favorites) {
            List<MenuItem> items = specials.getItems();

            Cache.addOrUpdateItems(items, false);
            Timber.d("Found %d specials.", items.size());

            return specials;
          }
        })
        .cache();
    }

    return specials;
  }

  /**
   * Creates an observable that, when subscribed to, fetches both the current menu and the user's favorites, then uses both data
   * sets to create the menu with each item that's in the user's favorites list properly flagged as a favorite.
   *
   * @return Observable to fetch menu
   */
  public static Observable<Menu> getMenu() {
    if (menu == null) {
      menu = service.getMenu()
        .subscribeOn(Schedulers.io()) // Use IO threads for network request.
        .observeOn(Schedulers.computation()) // Use computation threads for processing data.
        .zipWith(Favorites.getFavorites(), new Func2<Menu, CanonicalSet<MenuItem>, Menu>() {
          @Override
          public Menu call(Menu menu, CanonicalSet<MenuItem> favorites) {
            // Iterate through all sections.
            for (MenuSection section : menu.getSections()) {
              List<MenuItem> items = section.getItems();

              Cache.addOrUpdateItems(items, false);
              Timber.d("Found %d items in %s section.", items.size(), section.getName());
            }

            return menu;
          }
        })
        .cache();
    }

    return menu;
  }

  // @todo Make backend query when item doesn't exist.
  public static Observable<MenuItem> getItem(Object key) {
    return Cache.getItem(key);
  }

  public interface Service {
    @GET("specials.json")
    Observable<Specials> getSpecials();

    @GET("menu.json")
    Observable<Menu> getMenu();
  }
}
