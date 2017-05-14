package ru.nobird.github.search.ui.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.R;
import ru.nobird.github.search.data.model.SearchItem;
import ru.nobird.github.search.databinding.SearchItemBinding;

public class SearchItemsAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder>  {
    private final List<SearchItem> items;

    public SearchItemsAdapter() {
        this.items = new ArrayList<>();
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchItemViewHolder holder, int position) {
        holder.binding.searchItemLogin.setText(items.get(position).getLogin());
    }

    public void addItems(final List<SearchItem> list) {
        final int start = items.size();
        items.addAll(list);
        notifyItemRangeChanged(start, list.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder {
        private final SearchItemBinding binding;

        SearchItemViewHolder(final SearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
