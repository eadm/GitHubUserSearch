package ru.nobird.github.search.ui.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.data.model.UserSearchItem;
import ru.nobird.github.search.databinding.UserSearchItemBinding;
import ru.nobird.github.search.ui.fragment.FragmentMgr;
import ru.nobird.github.search.ui.fragment.UserFragment;

public class SearchItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int PROGRESS_VIEW_TYPE = 1;
    private final static int SEARCH_ITEM_VIEW_TYPE = 2;

    private final List<UserSearchItem> items;

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
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_search_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hld, int position) {
        if (hld instanceof SearchItemViewHolder) {
            final SearchItemViewHolder holder = (SearchItemViewHolder) hld;
            final UserSearchItem item = items.get(position);
            final Context context = holder.binding.getRoot().getContext();

            holder.binding.searchItemLogin.setText(item.getLogin());
            Glide.with(context).
                    load(items.get(position).getAvatarUrl()).
                    into(holder.binding.searchItemAvatar);

            holder.binding.searchItemContainer.setOnClickListener((v) ->
                            FragmentMgr.getInstance().addFragment(0, UserFragment.newInstance(item), true)
            );
        }
    }

    public void addItems(final List<UserSearchItem> list) {
        setLoading(false);
        final int start = items.size();
        items.addAll(list);
        notifyItemRangeChanged(start, list.size());
        hasMore = list.size() == API.PER_PAGE;
        page++;
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
            }
        } else {
            if (size > 0 && items.get(size - 1) == null) {
                items.remove(size - 1);
                notifyItemRemoved(size - 1);
                loading = false;
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

    public static class SearchItemViewHolder extends RecyclerView.ViewHolder {
        public final UserSearchItemBinding binding;

        public SearchItemViewHolder(final UserSearchItemBinding binding) {
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
