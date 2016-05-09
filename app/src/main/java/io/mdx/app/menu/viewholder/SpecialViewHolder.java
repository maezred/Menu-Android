package io.mdx.app.menu.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.R;
import io.mdx.app.menu.model.SpecialItem;

/**
 * Created by moltendorf on 16/5/4.
 */
public class SpecialViewHolder extends RecyclerViewAdapter.ViewHolder<SpecialItem> {
  public SpecialViewHolder(Context context, ViewGroup viewGroup) {
    super(context, viewGroup, R.layout.item_special);
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
  }

  public static class Factory extends RecyclerViewAdapter.Factory<SpecialViewHolder> {
    @Override
    public SpecialViewHolder createViewHolder(Context context, ViewGroup parent) {
      return new SpecialViewHolder(context, parent);
    }
  }
}
