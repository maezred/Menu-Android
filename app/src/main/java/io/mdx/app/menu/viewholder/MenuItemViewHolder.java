package io.mdx.app.menu.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.R;
import io.mdx.app.menu.model.MenuItem;

/**
 * Created by moltendorf on 16/5/9.
 */
public class MenuItemViewHolder extends RecyclerViewAdapter.ViewHolder<MenuItem> {
  public MenuItemViewHolder(Context context, ViewGroup viewGroup) {
    super(context, viewGroup, R.layout.item_menu);
  }

  @Override
  public void bindTo() {
    TextView  name        = (TextView) itemView.findViewById(R.id.item_special_name);
    TextView  price       = (TextView) itemView.findViewById(R.id.item_special_price);
    TextView  description = (TextView) itemView.findViewById(R.id.item_special_description);
    ImageView picture     = (ImageView) itemView.findViewById(R.id.item_special_picture);

    name.setText(object.getName());
    price.setText(object.getPrice());
    description.setText(object.getDescription());

    if (object.getPicture() != null && !object.getPicture().isEmpty()) {
      Picasso.with(context).load(object.getPicture()).fit().centerCrop().into(picture);
    }
  }

  public static class Factory extends RecyclerViewAdapter.Factory<MenuItemViewHolder> {
    @Override
    public MenuItemViewHolder createViewHolder(Context context, ViewGroup parent) {
      return new MenuItemViewHolder(context, parent);
    }
  }
}
