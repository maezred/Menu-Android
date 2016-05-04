package io.mdx.app.menu.viewholder;

import android.content.Context;
import android.view.ViewGroup;

import io.mdx.app.menu.R;
import io.mdx.app.menu.RecyclerViewAdapter;

/**
 * Created by moltendorf on 16/5/4.
 */
public class SpecialViewHolder extends RecyclerViewAdapter.ViewHolder {
  public SpecialViewHolder(Context context, ViewGroup viewGroup) {
    super(context, viewGroup, R.layout.item_special);
  }

  @Override
  public void bindTo(Object object, int position) {

  }

  public static class Factory implements RecyclerViewAdapter.Factory<SpecialViewHolder> {
    @Override
    public SpecialViewHolder createViewHolder(Context context, ViewGroup parent) {
      return new SpecialViewHolder(context, parent);
    }
  }
}
