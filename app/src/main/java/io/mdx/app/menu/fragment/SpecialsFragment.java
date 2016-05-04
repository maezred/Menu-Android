package io.mdx.app.menu.fragment;

/**
 * Created by moltendorf on 16/4/29.
 */
public class SpecialsFragment extends MenuFragment {
  public static final String ACTION_SPECIALS = "io.mdx.app.menu.SPECIALS";

  private static FragmentType TYPE = FragmentType.SPECIALS;

  public SpecialsFragment() {
    super(TYPE);
  }

  public static class Factory implements FragmentFactory<SpecialsFragment> {
    @Override
    public FragmentType getType() {
      return TYPE;
    }

    @Override
    public SpecialsFragment newInstance() {
      return new SpecialsFragment();
    }
  }
}
