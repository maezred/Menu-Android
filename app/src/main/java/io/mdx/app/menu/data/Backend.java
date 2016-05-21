package io.mdx.app.menu.data;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

  private static Service service;

  static {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://mexxis.mdx.co/data/")
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();

    service = retrofit.create(Service.class);
  }

  // @todo This is basically the same method as getMenu. Compact?

  /**
   * Creates an observable that, when subscribed to, fetches both the current specials and the user's favorites, then uses both
   * data sets to create a list of specials with each special that's in the user's favorites list properly flagged as a
   * favorite.
   *
   * @return Observable to fetch Specials
   */
  public static Observable<Specials> getSpecials() {
    return service.getSpecials()
      .subscribeOn(Schedulers.io()) // Use IO threads for network request.
      .observeOn(Schedulers.computation()) // Use computation threads for processing data.
      .zipWith(Favorites.getFavorites(), new Func2<Specials, Set<MenuItem>, Specials>() {
        @Override
        public Specials call(Specials specials, Set<MenuItem> favorites) {
          Map<String, MenuItem> favoriteLookup = new HashMap<>();

          // Index the favorites in a map for quick lookup.
          for (MenuItem favorite : favorites) {
            favoriteLookup.put(favorite.getName(), favorite);
          }

          // Iterate through all specials and check if they're in the favorites map.
          for (MenuItem special : specials.getItems()) {
            MenuItem favorite = favoriteLookup.get(special.getName());

            if (favorite != null) {
              special.setFavorite(true); // Mark this special as favorite.

              // Check if the favorite's extra data matches.
              if (!favorite.equals(special)) {
                Favorites.updateFavorite(special); // Update the favorite's extra data.
              }
            }
          }

          return specials;
        }
      });
  }

  // @todo This is basically the same method as getSpecials. Compact?

  /**
   * Creates an observable that, when subscribed to, fetches both the current menu and the user's favorites, then uses both data
   * sets to create the menu with each item that's in the user's favorites list properly flagged as a favorite.
   *
   * @return Observable to fetch menu
   */
  public static Observable<Menu> getMenu() {
    return service.getMenu()
      .subscribeOn(Schedulers.io()) // Use IO threads for network request.
      .observeOn(Schedulers.computation()) // Use computation threads for processing data.
      .zipWith(Favorites.getFavorites(), new Func2<Menu, Set<MenuItem>, Menu>() {
        @Override
        public Menu call(Menu menu, Set<MenuItem> favorites) {
          Map<String, MenuItem> favoriteLookup = new HashMap<>();

          // Index the favorites in a map for quick lookup.
          for (MenuItem favorite : favorites) {
            favoriteLookup.put(favorite.getName(), favorite);
          }

          // Iterate through all items and check if they're in the favorites map.
          for (MenuSection section : menu.getSections()) {
            for (MenuItem item : section.getItems()) {
              MenuItem favorite = favoriteLookup.get(item.getName());

              if (favorite != null) {
                item.setFavorite(true); // Mark this special as favorite.

                // Check if the favorite's extra data matches.
                if (!favorite.equals(item)) {
                  Favorites.updateFavorite(item); // Update the favorite's extra data.
                }
              }
            }
          }

          return menu;
        }
      });
  }

  public interface Service {
    @GET("specials.json")
    Observable<Specials> getSpecials();

    @GET("menu.json")
    Observable<Menu> getMenu();
  }
}
