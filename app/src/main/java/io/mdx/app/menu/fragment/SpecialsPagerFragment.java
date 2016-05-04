package io.mdx.app.menu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/4/29.
 */
public class SpecialsPagerFragment extends BaseFragment {
  public static final String ACTION_SPECIALS = "io.mdx.app.menu.SPECIALS";

  private static FragmentType TYPE = FragmentType.SPECIALS;

  private SpecialsPagerAdapter specialsPagerAdapter;
  private ViewPager            viewPager;

  public SpecialsPagerFragment() {
    super(TYPE, R.layout.fragment_specials_pager);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupViewPager();
  }

  private void setupViewPager() {
    specialsPagerAdapter = new SpecialsPagerAdapter(getChildFragmentManager());

    viewPager = (ViewPager) getView();
    viewPager.setAdapter(specialsPagerAdapter);
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SpecialsPagerAdapter extends FragmentPagerAdapter {
    private FragmentFactory[] factories = new FragmentFactory[]{
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory(),
      new SpecialFragment.Factory()
    };

    public SpecialsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return factories[position].newInstance();
    }

    @Override
    public int getCount() {
      return factories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return factories[position].getType().getPageTitle();
    }
  }

  public static class Factory implements FragmentFactory<SpecialsPagerFragment> {
    @Override
    public FragmentType getType() {
      return TYPE;
    }

    @Override
    public SpecialsPagerFragment newInstance() {
      return new SpecialsPagerFragment();
    }
  }
}
