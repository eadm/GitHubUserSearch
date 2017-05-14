package ru.nobird.github.search;

import android.app.Application;

import ru.nobird.github.search.api.API;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        API.init();
    }
}
