package ru.nobird.github.search.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.R;
import ru.nobird.github.search.data.model.User;
import ru.nobird.github.search.ui.fragment.FragmentMgr;
import ru.nobird.github.search.ui.fragment.UserFragment;

public class BookmarksAdapter extends RecyclerView.Adapter<SearchItemsAdapter.SearchItemViewHolder> {

    private final List<User> items;

    public BookmarksAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public SearchItemsAdapter.SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchItemsAdapter.SearchItemViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchItemsAdapter.SearchItemViewHolder holder, int position) {
        final User item = items.get(position);
        final Context context = holder.binding.getRoot().getContext();

        holder.binding.searchItemLogin.setText(item.getLogin());
        Glide.with(context).
                load(items.get(position).getAvatarUrl()).
                into(holder.binding.searchItemAvatar);

        holder.binding.searchItemContainer.setOnClickListener((v) ->
                FragmentMgr.getInstance().replaceFragment(0, UserFragment.newInstance(item), true)
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(final List<User> list) {
        final int start = items.size();
        items.addAll(list);
        notifyItemRangeChanged(start, list.size());
    }
}
