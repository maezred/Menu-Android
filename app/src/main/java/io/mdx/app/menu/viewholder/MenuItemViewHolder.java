package io.mdx.app.menu.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Database;
import io.mdx.app.menu.model.MenuItem;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/5/9.
 */
public class MenuItemViewHolder extends RecyclerViewAdapter.ViewHolder<MenuItem> {
  public MenuItemViewHolder(Context context, ViewGroup viewGroup) {
    super(context, viewGroup, R.layout.item_menu);

    RxView.clicks(itemView.findViewById(R.id.item_special_favorite)).subscribe(new Action1<Void>() {
      @Override
      public void call(Void aVoid) {
        boolean favorite = object.getFavorite();

        if (favorite) {
          Database.removeFavorite(object);
        } else {
          Database.addFavorite(object);
        }

        object.setFavorite(!favorite);
      }
    });
  }

  @Override
  public void bindTo() {
    TextView  name        = (TextView) itemView.findViewById(R.id.item_special_name);
    TextView  price       = (TextView) itemView.findViewById(R.id.item_special_price);
    TextView  description = (TextView) itemView.findViewById(R.id.item_special_description);
    ImageView picture     = (ImageView) itemView.findViewById(R.id.item_special_picture);
    CheckBox  favorite    = (CheckBox) itemView.findViewById(R.id.item_special_favorite);

    name.setText(object.getName());
    price.setText(object.getPrice());
    description.setText(object.getDescription());

    if (object.getPicture() != null && !object.getPicture().isEmpty()) {
      Picasso.with(context).load(object.getPicture()).fit().centerCrop().into(picture);
    }

    favorite.setChecked(object.getFavorite());
  }

  public static class Factory extends RecyclerViewAdapter.Factory<MenuItemViewHolder> {
    @Override
    public MenuItemViewHolder createViewHolder(Context context, ViewGroup parent) {
      return new MenuItemViewHolder(context, parent);
    }
  }
}
