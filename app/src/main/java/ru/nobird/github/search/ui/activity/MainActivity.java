package ru.nobird.github.search.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import ru.nobird.github.search.R;
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
            FragmentMgr.getInstance().addFragment(R.id.activity_main_fragment_container, new SearchFragment(), false);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
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

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.START);
        }
    }
}
