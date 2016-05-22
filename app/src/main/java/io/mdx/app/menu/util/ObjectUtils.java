package io.mdx.app.menu.util;

/**
 * Created by moltendorf on 16/5/21.
 */
public class ObjectUtils {
  public static boolean equals(Object a, Object b) {
    return a == b || (a != null && a.equals(b));
  }
}
