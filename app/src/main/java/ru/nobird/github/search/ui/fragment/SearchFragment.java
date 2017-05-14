package ru.nobird.github.search.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapism.searchview.SearchView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.api.UserSearchResponse;
import ru.nobird.github.search.databinding.FragmentSearchBinding;
import ru.nobird.github.search.ui.adapter.SearchItemsAdapter;

public class SearchFragment extends Fragment {
    private SearchItemsAdapter adapter;
    private FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        setSearchView(binding.fragmentSearchQuery);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.fragmentSearchToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);


        binding.fragmentSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentSearchResults.setAdapter(adapter);
        binding.fragmentSearchResults.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!adapter.isLoading() && adapter.hasMore()) {
                    final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager.findLastCompletelyVisibleItemPosition()
                            == layoutManager.getItemCount() - 2) {
                        loadSearchResult();
                    }
                }
            }
        });

        if (!adapter.isLoading() && adapter.getQuery().isEmpty()) {
            binding.fragmentSearchIcon.setVisibility(View.VISIBLE);
        } else {
            binding.fragmentSearchIcon.setVisibility(View.GONE);
        }

        if (!adapter.isLoading() && adapter.getItemCount() == 0 && !adapter.getQuery().isEmpty()) {
            binding.fragmentSearchEmptyResult.setVisibility(View.VISIBLE);
        } else {
            binding.fragmentSearchEmptyResult.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        adapter = new SearchItemsAdapter();
    }

    private void setSearchView(final SearchView searchView) {
        searchView.setHint(R.string.search_for_users);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!adapter.getQuery().equals(s)) {
                    if (s.isEmpty()) {
                        adapter.clear();
                        binding.fragmentSearchIcon.setVisibility(View.VISIBLE);
                    } else {
                        searchView.close(false);
                        adapter.setQuery(s);
                        binding.fragmentSearchIcon.setVisibility(View.GONE);
                        binding.fragmentSearchEmptyResult.setVisibility(View.GONE);
                        loadSearchResult();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);

        searchView.setOnMenuClickListener(() -> {
            // todo open drawer
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
    }

    private void loadSearchResult() {
        adapter.setLoading(true);
        API.getInstance()
                .searchUsers(adapter.getQuery(), adapter.getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSearchResult, Throwable::printStackTrace);
    }

    private void onSearchResult(final UserSearchResponse response) {
        adapter.addItems(response.getItems());
        if (binding != null && adapter.getItemCount() == 0) {
            binding.fragmentSearchEmptyResult.setVisibility(View.VISIBLE);
        }
    }
}
