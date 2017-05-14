package ru.nobird.github.search;

import android.app.Application;

import ru.nobird.github.search.api.API;
import ru.nobird.github.search.data.db.DBMgr;
import ru.nobird.github.search.ui.fragment.FragmentMgr;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        API.init();
        FragmentMgr.init();
        DBMgr.init(getApplicationContext());
    }
}
