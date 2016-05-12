package io.mdx.app.menu.data;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

import io.mdx.app.menu.model.Menu;
import io.mdx.app.menu.model.Specials;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

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

  public interface Service {
    @GET("specials.json")
    Observable<Specials> getSpecials();

    @GET("menu.json")
    Observable<Menu> getMenu();
  }
}