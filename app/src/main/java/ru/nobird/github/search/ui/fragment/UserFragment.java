package ru.nobird.github.search.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.nobird.github.search.data.model.SearchItem;

public class UserFramgent extends Fragment {
    private static final String ARG_SEARCH_ITEM = "search_item";

    public static Fragment newInstance(final SearchItem item) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SEARCH_ITEM, item);
        final Fragment fragment = new UserFramgent();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
