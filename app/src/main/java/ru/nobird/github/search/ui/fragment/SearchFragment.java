package ru.nobird.github.search.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.api.UserSearchResponse;
import ru.nobird.github.search.databinding.FragmentSearchBinding;
import ru.nobird.github.search.ui.activity.MainActivity;
import ru.nobird.github.search.ui.adapter.SearchItemsAdapter;

public class SearchFragment extends Fragment {
    private SearchItemsAdapter adapter;
    private FragmentSearchBinding binding;
    private Disposable disposable;
    private SearchHistoryTable searchHistoryTable;

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
        searchHistoryTable = new SearchHistoryTable(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void setSearchView(final SearchView searchView) {
        searchView.setHint(R.string.search_for_users);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryText(searchView, s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
        searchView.setVoice(false);
        searchView.close(false);
        searchView.setOnMenuClickListener(() -> ((MainActivity) getActivity()).openDrawer());
        final SearchAdapter searchAdapter = new SearchAdapter(getContext());
        searchAdapter.setOnSearchItemClickListener((view, position) -> {
            final TextView textView = (TextView) view.findViewById(R.id.textView);
            final String query = textView.getText().toString();
            searchView.setQuery(query, false);
            onQueryText(searchView, query);
        });
        searchView.setAdapter(searchAdapter);
    }

    private void onQueryText(final SearchView searchView, final String s) {
        if (!adapter.getQuery().equals(s)) {
            if (s.isEmpty()) {
                adapter.clear();
                binding.fragmentSearchIcon.setVisibility(View.VISIBLE);
            } else {
                searchView.close(false);
                adapter.setQuery(s);
                binding.fragmentSearchIcon.setVisibility(View.GONE);
                binding.fragmentSearchEmptyResult.setVisibility(View.GONE);
                searchHistoryTable.addItem(new SearchItem(s));
                loadSearchResult();
            }
        }
    }

    @Override
    public void onDestroy() {
        adapter = null;
        if (disposable != null) disposable.dispose();
        searchHistoryTable = null;
        super.onDestroy();
    }

    private void loadSearchResult() {
        adapter.setLoading(true);
        disposable = API.getInstance()
                .searchUsers(adapter.getQuery(), adapter.getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSearchResult, this::handleError);
    }

    private void onSearchResult(final UserSearchResponse response) {
        adapter.addItems(response.getItems());
        if (binding != null && adapter.getItemCount() == 0) {
            binding.fragmentSearchEmptyResult.setVisibility(View.VISIBLE);
        }
    }

    private void handleError(final Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }

        adapter.setLoading(false);

        if (binding != null) {
            Snackbar.make(binding.getRoot(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
            if (adapter.getItemCount() == 0) binding.fragmentSearchIcon.setVisibility(View.VISIBLE);
        }
    }
}
