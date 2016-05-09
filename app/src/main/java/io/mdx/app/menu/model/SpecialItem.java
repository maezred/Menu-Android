package io.mdx.app.menu.model;

/**
 * Created by moltendorf on 16/5/4.
 */
public class SpecialItem {
  private String  _name;
  private String  _price;
  private String  _description;
  private String  _picture;
  private Boolean _display;

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
}
