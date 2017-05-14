package ru.nobird.github.search.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.R;
import ru.nobird.github.search.data.model.Repo;
import ru.nobird.github.search.databinding.RepoItemBinding;
import ru.nobird.github.search.ui.helper.UIHelper;

public class RepoItemsAdapter extends RecyclerView.Adapter<RepoItemsAdapter.RepoViewHolder> {
    private final List<Repo> items;


    public RepoItemsAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepoViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.repo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        final Repo repo = items.get(position);
        holder.binding.repoItemName.setText(repo.getName());

        UIHelper.setTextOrHideIfNull(holder.binding.repoItemDescription, repo.getDescription());
        UIHelper.setTextOrHideIfNull(holder.binding.repoItemLang, repo.getLanguage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(final List<Repo> list) {
        final int start = items.size();
        items.addAll(list);
        notifyItemRangeChanged(start, list.size());
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {
        private final RepoItemBinding binding;


        RepoViewHolder(final RepoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

//        private final RepoItemBinding
    }

}
