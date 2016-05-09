package io.mdx.app.menu.model;

import java.util.List;

/**
 * Created by moltendorf on 16/5/9.
 */
public class Specials {
  private Long              _updated;
  private List<SpecialItem> _items;

  public Long getUpdated() {
    return _updated;
  }

  public List<SpecialItem> getItems() {
    return _items;
  }
}
