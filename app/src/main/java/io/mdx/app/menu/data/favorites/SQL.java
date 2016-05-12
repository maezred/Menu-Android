package io.mdx.app.menu.data.favorites;

/**
 * Created by moltendorf on 16/5/12.
 */
interface SQL {
  String CREATE_TABLE = "CREATE TABLE " + Favorites.TABLE + " (" +
    Favorites.C_NAME + " VARCHAR(255) PRIMARY KEY, " +
    Favorites.C_PRICE + " VARCHAR(255) DEFAULT NULL, " +
    Favorites.C_DESCRIPTION + " VARCHAR(255) DEFAULT NULL," +
    Favorites.C_PICTURE + " VARCHAR(255) DEFAULT NULL" +
    ")";

  String GET_FAVORITES = "SELECT * " +
    "FROM " + Favorites.TABLE;

  String IS_FAVORITE = "SELECT 1 " +
    "FROM " + Favorites.TABLE + " " +
    "WHERE " + Favorites.C_NAME + " = ?";

  String ADD_FAVORITE = "INSERT INTO " + Favorites.TABLE + " " +
    "(" + Favorites.C_NAME + ", " + Favorites.C_PRICE + ", " + Favorites.C_DESCRIPTION + ", " + Favorites.C_PICTURE + ") " +
    "VALUES (?, ?, ?, ?)";

  String REMOVE_FAVORITE = "DELETE FROM " + Favorites.TABLE + " " +
    "WHERE " + Favorites.C_NAME + " = ?";
}
