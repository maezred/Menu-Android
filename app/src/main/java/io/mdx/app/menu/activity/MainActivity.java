package io.mdx.app.menu.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import io.mdx.app.menu.R;
import io.mdx.app.menu.fragment.FragmentFactory;
import io.mdx.app.menu.fragment.MenuFragment;
import io.mdx.app.menu.fragment.SpecialsFragment;

public class MainActivity extends BaseActivity {
  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private MainPagerAdapter mainPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager viewPager;

  public MainActivity() {
    super(R.layout.activity_main);
  }

  @Override
  protected void onViewCreated() {
    super.onViewCreated();

    setupViewPager();
  }

  private void setupViewPager() {
    mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

    viewPager = (ViewPager) findViewById(R.id.container);
    viewPager.setAdapter(mainPagerAdapter);

    TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class MainPagerAdapter extends FragmentPagerAdapter {
    private FragmentFactory[] factories = new FragmentFactory[]{
      new SpecialsFragment.Factory(),
      new MenuFragment.Factory()
    };

    public MainPagerAdapter(FragmentManager fm) {
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
}
