package io.mdx.app.menu.model;

import android.view.MenuItem;

import java.util.List;

/**
 * Created by moltendorf on 16/5/9.
 */
public class MenuSection {
  private String         _name;
  private List<MenuItem> _items;

  public String getName() {
    return _name;
  }

  public List<MenuItem> getItems() {
    return _items;
  }
}
