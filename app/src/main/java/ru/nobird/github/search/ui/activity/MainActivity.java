package ru.nobird.github.search.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import ru.nobird.github.search.R;
import ru.nobird.github.search.ui.fragment.BookmarkFragment;
import ru.nobird.github.search.ui.fragment.FragmentMgr;
import ru.nobird.github.search.ui.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentMgr.getInstance().attach(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentMgr.getInstance().addFragment(0, new SearchFragment(), false);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_navigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.drawer_search:
                    fragment = new SearchFragment();
                break;
                case R.id.drawer_bookmarks:
                    fragment = new BookmarkFragment();
                break;
            }
            if (fragment != null) {
                drawerLayout.closeDrawer(Gravity.START);
                FragmentMgr.getInstance().clear();
                FragmentMgr.getInstance().replaceFragment(0, fragment, false);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                openDrawer();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSupportActionBarWithToggle(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (toolbar != null && drawerLayout != null) {
            final ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();
            toggle.setDrawerIndicatorEnabled(true);
        }
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }
}
