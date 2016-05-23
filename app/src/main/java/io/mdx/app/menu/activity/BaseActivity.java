package io.mdx.app.menu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.mdx.app.menu.R;

/**
 * Created by moltendorf on 16/4/29.
 */
abstract public class BaseActivity extends RxAppCompatActivity {
  protected int layout;

  protected BaseActivity(int layout) {
    super();

    this.layout = layout;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout);

    onViewCreated();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();


    if (id == R.id.action_settings) {
      startActivity(new Intent(SettingsActivity.ACTION_SETTINGS));

      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  protected void onViewCreated() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }
}
