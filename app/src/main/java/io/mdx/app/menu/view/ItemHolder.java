package io.mdx.app.menu.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.R;
import io.mdx.app.menu.data.favorites.Favorites;
import io.mdx.app.menu.model.MenuItem;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/5/9.
 */
public class ItemHolder extends RecyclerViewAdapter.ViewHolder<MenuItem> {
  public ItemHolder(Context context, ViewGroup viewGroup, int resource) {
    super(context, viewGroup, resource);

    RxView.clicks(itemView.findViewById(R.id.item_favorite))
      .subscribe(new Action1<Void>() {
        @Override
        public void call(Void aVoid) {
          boolean favorite = object.getFavorite();

          if (favorite) {
            Favorites.removeFavorite(object);
          } else {
            Favorites.addFavorite(object);
          }

          object.setFavorite(!favorite);
        }
      });
  }

  @Override
  public void bindTo() {
    TextView  name        = (TextView) itemView.findViewById(R.id.item_name);
    TextView  price       = (TextView) itemView.findViewById(R.id.item_price);
    TextView  description = (TextView) itemView.findViewById(R.id.item_description);
    ImageView picture     = (ImageView) itemView.findViewById(R.id.item_picture);
    CheckBox  favorite    = (CheckBox) itemView.findViewById(R.id.item_favorite);

    name.setText(object.getName());
    price.setText(object.getPrice());
    description.setText(object.getDescription());

    Picasso.with(context).cancelRequest(picture);

    if (object.getPicture() != null && !object.getPicture().isEmpty()) {
      Picasso.with(context).load(object.getPicture()).into(picture);
    } else {
      picture.setImageResource(android.R.color.transparent);
    }

    favorite.setChecked(object.getFavorite());
  }

  public static class Factory extends RecyclerViewAdapter.Factory<ItemHolder> {
    private int resource;

    public Factory(int resource) {
      this.resource = resource;
    }

    @Override
    public ItemHolder createViewHolder(Context context, ViewGroup parent) {
      return new ItemHolder(context, parent, resource);
    }
  }
}
