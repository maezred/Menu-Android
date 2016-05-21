package io.mdx.app.menu.model;

import android.database.Cursor;

import io.mdx.app.menu.data.favorites.Favorites;

/**
 * Created by moltendorf on 16/5/9.
 */
public class MenuItem {
  transient private boolean favorite = false;

  private String  _name;
  private String  _price;
  private String  _description;
  private String  _picture;
  private Boolean _display;

  public MenuItem(Cursor cursor) {
    _name = cursor.getString(cursor.getColumnIndex(Favorites.C_NAME));
    _price = cursor.getString(cursor.getColumnIndex(Favorites.C_PRICE));
    _description = cursor.getString(cursor.getColumnIndex(Favorites.C_DESCRIPTION));
    _picture = cursor.getString(cursor.getColumnIndex(Favorites.C_PICTURE));

    favorite = true;
  }

  public int getId() {
    return hashCode(); // Simple hack for now.
  }

  public String getName() {
    return _name;
  }

  public String getPrice() {
    return _price;
  }

  public String getDescription() {
    return _description;
  }

  public String getPicture() {
    return _picture;
  }

  public Boolean getDisplay() {
    return _display;
  }

  public boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null) {
      return false;
    }

    Class<?> type = o.getClass();

    if (getClass() == type) {
      MenuItem menuItem = (MenuItem) o;

      return _name.equals(menuItem._name);
    } else if (Integer.class == type) {
      return getId() == o;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return _name.hashCode();
  }
}
