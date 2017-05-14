package ru.nobird.github.search.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.data.model.SearchItem;
import ru.nobird.github.search.databinding.SearchItemBinding;

public class SearchItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final static int PROGRESS_VIEW_TYPE = 1;
    private final static int SEARCH_ITEM_VIEW_TYPE = 2;

    private final List<SearchItem> items;

    private String query = "";
    private int page;

    private boolean loading = false;
    private boolean hasMore = false;

    public SearchItemsAdapter() {
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) == null) ? PROGRESS_VIEW_TYPE : SEARCH_ITEM_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PROGRESS_VIEW_TYPE) {
            return new ProgressViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false));
        } else {
            return new SearchItemViewHolder(
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.search_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchItemViewHolder) {
            ((SearchItemViewHolder) holder).binding.searchItemLogin.setText(items.get(position).getLogin());
            final ImageView imageView = ((SearchItemViewHolder) holder).binding.searchItemAvatar;
            Glide.with(imageView.getContext()).
                    load(items.get(position).getAvatarUrl()).
                    into(imageView);
        }
    }

    public void addItems(final List<SearchItem> list) {
        setLoading(false);
        final int start = items.size();
        items.addAll(list);
        notifyItemRangeChanged(start, list.size());
        hasMore = list.size() == API.PER_PAGE;
        page++;
        Log.d("addItems", "hasMore-" + hasMore + " page-" + page + " size-" + list.size());
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void setLoading(final boolean isLoading) {
        final int size = items.size();
        if (isLoading) {
            if (size == 0 || items.get(size - 1) != null) {
                items.add(null);
                notifyItemInserted(size);
                loading = true;

                Log.d("setLoading", "true");
            }
        } else {
            if (size > 0 && items.get(size - 1) == null) {
                items.remove(size - 1);
                notifyItemRemoved(size - 1);
                loading = false;
                Log.d("setLoading", "false");
            }
        }
    }

    public void setQuery(final String query) {
        this.query = query;
        this.page = 1;
        clear();
    }

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class SearchItemViewHolder extends RecyclerView.ViewHolder {
        private final SearchItemBinding binding;

        private SearchItemViewHolder(final SearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressViewHolder(final View view) {
            super(view);
        }
    }
}
