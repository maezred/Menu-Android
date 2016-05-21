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

      boolean update = false;

      if (!existing.getName().equals(item.getName())) {
        existing.setName(item.getName());

        update = true;
      }

      if (!existing.getPrice().equals(item.getPrice())) {
        existing.setPrice(item.getPrice());

        update = true;
      }

      if (!existing.getDescription().equals(item.getDescription())) {
        existing.setDescription(item.getDescription());

        update = true;
      }

      if (!existing.getPicture().equals(item.getPicture())) {
        existing.setPicture(item.getPicture());

        update = true;
      }

      if (!existing.getDisplay().equals(item.getDisplay())) {
        existing.setDisplay(item.getDisplay());

        update = true;
      }

      if (update && existing.getFavorite()) {
        Favorites.updateFavorite(existing);
      }

      return existing;
    }

    return item;
  }
}
