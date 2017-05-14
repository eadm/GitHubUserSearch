package ru.nobird.github.search.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapism.searchview.SearchView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.databinding.FragmentSearchBinding;
import ru.nobird.github.search.ui.adapter.SearchItemsAdapter;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchItemsAdapter adapter;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        searchView = binding.fragmentSearchQuery;
        setSearchView();

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.fragmentSearchToolbar);


        binding.fragmentSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentSearchResults.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        adapter = new SearchItemsAdapter();
    }

    private void setSearchView() {
        searchView.setHint(R.string.search_for_users);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                API.getInstance()
                        .searchUsers(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((r) -> adapter.addItems(r.getItems()), Throwable::printStackTrace);
                searchView.close(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
