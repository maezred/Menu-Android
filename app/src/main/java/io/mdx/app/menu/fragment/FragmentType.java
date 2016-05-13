package io.mdx.app.menu.fragment;

/**
 * Created by moltendorf on 16/4/29.
 */
public enum FragmentType {
  MENU("Menu"),
  SPECIALS("Specials"),
  FAVORITES("Favorites");

  final private String pageTitle;

  FragmentType(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public String getPageTitle() {
    return pageTitle;
  }
}
