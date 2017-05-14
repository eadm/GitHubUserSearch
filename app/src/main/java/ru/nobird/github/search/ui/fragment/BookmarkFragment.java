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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.data.db.DBMgr;
import ru.nobird.github.search.databinding.FragmentBookmarkBinding;
import ru.nobird.github.search.ui.adapter.BookmarksAdapter;

public class BookmarkFragment extends Fragment {

    private BookmarksAdapter adapter;
    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new BookmarksAdapter();

        disposable = Observable.fromCallable(DBMgr.getInstance()::getBookmarks)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(adapter::addItems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final FragmentBookmarkBinding binding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.fragmentBookmarkToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.fragmentBookmarkUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentBookmarkUsers.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        adapter = null;
        super.onDestroy();
    }
}
