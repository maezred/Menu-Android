package io.mdx.app.menu.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

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
  private SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;

  public MainActivity() {
    super(R.layout.activity_main);
  }

  @Override
  protected void onViewCreated() {
    super.onViewCreated();

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });

    createTabs();
  }

  private void createTabs() {
    TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
    tabs.setupWithViewPager(mViewPager);
  }


  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private FragmentFactory[] factories = new FragmentFactory[]{
      new SpecialsFragment.Factory(),
      new MenuFragment.Factory()
    };

    public SectionsPagerAdapter(FragmentManager fm) {
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
