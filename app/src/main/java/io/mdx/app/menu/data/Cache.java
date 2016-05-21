package io.mdx.app.menu.data;

import io.mdx.app.menu.data.favorites.Favorites;
import io.mdx.app.menu.model.MenuItem;

/**
 * Created by moltendorf on 16/5/21.
 */
public class Cache {
  private static CanonicalSet<MenuItem> items = new CanonicalSet<>();

  public static MenuItem getItem(Object item) {
    return items.get(item);
  }

  public static MenuItem addOrUpdateItem(MenuItem item) {
    if (!items.add(item)) {
      MenuItem existing = items.get(item);

      existing.setName(item.getName());
      existing.setPrice(item.getPrice());
      existing.setDescription(item.getDescription());
      existing.setPicture(item.getPicture());
      existing.setDisplay(item.getDisplay());

      if (existing.getFavorite()) {
        Favorites.updateFavorite(existing);
      }

      return existing;
    }

    return item;
  }
}
