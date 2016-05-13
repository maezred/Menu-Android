package io.mdx.app.menu.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import net.moltendorf.android.recyclerviewadapter.RecyclerViewAdapter;

import io.mdx.app.menu.model.MenuSection;

/**
 * Created by moltendorf on 16/5/9.
 */
public class MenuSectionViewHolder extends RecyclerViewAdapter.ViewHolder<MenuSection> {
  public MenuSectionViewHolder(Context context, ViewGroup viewGroup, int resource) {
    super(context, viewGroup, resource);
  }

  @Override
  public void bindTo() {
    ((TextView) itemView).setText(object.getName());
  }

  public static class Factory extends RecyclerViewAdapter.Factory<MenuSectionViewHolder> {
    private int resource;

    public Factory(int resource) {
      this.resource = resource;
    }

    @Override
    public MenuSectionViewHolder createViewHolder(Context context, ViewGroup parent) {
      return new MenuSectionViewHolder(context, parent, resource);
    }
  }
}
