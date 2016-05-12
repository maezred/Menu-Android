package io.mdx.app.menu.data;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public static Service getService() {
    return service;
  }

  public static Observable<Specials> getSpecials() {
    return service.getSpecials().zipWith(Database.getFavorites(), new Func2<Specials, List<MenuItem>, Specials>() {
      @Override
      public Specials call(Specials specials, List<MenuItem> favorites) {
        Map<String, MenuItem> favoriteLookup = new HashMap<>();

        for (MenuItem favorite : favorites) {
          favoriteLookup.put(favorite.getName(), favorite);
        }

        for (MenuItem special : specials.getItems()) {
          if (favoriteLookup.containsKey(special.getName())) {
            special.setFavorite(true);
          }
        }

        return specials;
      }
    });
  }

  public static Observable<Menu> getMenu() {
    return service.getMenu().zipWith(Database.getFavorites(), new Func2<Menu, List<MenuItem>, Menu>() {
      @Override
      public Menu call(Menu menu, List<MenuItem> favorites) {
        Map<String, MenuItem> favoriteLookup = new HashMap<>();

        for (MenuItem favorite : favorites) {
          favoriteLookup.put(favorite.getName(), favorite);
        }

        for (MenuSection section : menu.getSections()) {
          for (MenuItem item : section.getItems()) {
            if (favoriteLookup.containsKey(item.getName())) {
              item.setFavorite(true);
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
