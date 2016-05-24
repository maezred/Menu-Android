package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.FragmentEvent;

import io.mdx.app.menu.Action;
import io.mdx.app.menu.R;
import io.mdx.app.menu.data.Cache;
import io.mdx.app.menu.model.MenuItem;
import rx.functions.Action1;

/**
 * Created by moltendorf on 16/5/19.
 */
public class DetailFragment extends BaseFragment {
  public static final Action ACTION_DETAIL = new Action("DETAIL");

  private static final String ITEM_ID = "ITEM_ID";

  public static Bundle createBundle(int itemId) {
    Bundle bundle = new Bundle();
    bundle.putInt(ITEM_ID, itemId);

    return bundle;
  }

  private int itemId = -1;

  public DetailFragment() {
    super(R.layout.fragment_detail);
  }

  @Override
  public void setArguments(Bundle args) {
    super.setArguments(args);

    if (args != null) {
      itemId = args.getInt(ITEM_ID, -1);
    }
  }

  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (itemId != -1) {
      Cache.getItem(itemId)
        .compose(this.<MenuItem>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(new Action1<MenuItem>() {
          @Override
          public void call(MenuItem item) {
            TextView  name        = (TextView) view.findViewById(R.id.item_name);
            TextView  price       = (TextView) view.findViewById(R.id.item_price);
            TextView  description = (TextView) view.findViewById(R.id.item_description);
            ImageView picture     = (ImageView) view.findViewById(R.id.item_picture);
            CheckBox  favorite    = (CheckBox) view.findViewById(R.id.item_favorite);

            name.setText(item.getName());
            price.setText(item.getPrice());
            description.setText(item.getDescription());

            if (item.getPicture() != null && !item.getPicture().isEmpty()) {
              Picasso.with(getContext()).load(item.getPicture()).fit().centerCrop().into(picture);
            } else {
              picture.setImageResource(android.R.color.transparent);
            }

            favorite.setChecked(item.getFavorite());
          }
        });
    }
  }
}
