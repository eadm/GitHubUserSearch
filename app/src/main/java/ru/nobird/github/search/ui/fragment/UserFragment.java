package ru.nobird.github.search.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.data.model.SearchItem;
import ru.nobird.github.search.data.model.User;
import ru.nobird.github.search.databinding.FragmentUserBinding;
import ru.nobird.github.search.ui.adapter.RepoItemsAdapter;

public class UserFragment extends Fragment {
    private static final String ARG_SEARCH_ITEM = "search_item";

    public static Fragment newInstance(final SearchItem item) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SEARCH_ITEM, item);
        final Fragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private CompositeDisposable compositeDisposable;
    private SearchItem item;
    private User user;
    private FragmentUserBinding binding;

    private RepoItemsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        compositeDisposable = new CompositeDisposable();
        adapter = new RepoItemsAdapter();

        item = getArguments().getParcelable(ARG_SEARCH_ITEM);
        if (item != null) {
            compositeDisposable.add(API.getInstance()
                    .getUser(item.getLogin())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setUser, this::handleError));

            compositeDisposable.add(API.getInstance()
                    .getUserRepos(item.getLogin())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(adapter::addItems, this::handleError)
            );
        }
    }


    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        adapter = null;
        super.onDestroy();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.fragmentUserToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (user != null) {
            setUser(user);
        } else if (item != null) {
            Glide.with(getContext()).load(item.getAvatarUrl()).into(binding.fragmentUserAvatar);
            Glide.with(getContext()).load(item.getAvatarUrl()).into(binding.fragmentUserAvatarSmall);

            binding.fragmentUserLogin.setText(item.getLogin());
            binding.fragmentUserLoginSmall.setText(item.getLogin());
        }

        binding.fragmentUserAppbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (binding.fragmentUserInfoContainer.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(binding.fragmentUserInfoContainer)) {
                binding.fragmentUserInfoSmall.animate().alpha(1).setDuration(getResources().getInteger(R.integer.animation_duration_fast));
            } else {
                binding.fragmentUserInfoSmall.animate().alpha(0).setDuration(getResources().getInteger(R.integer.animation_duration_fast));
            }
        });

        binding.fragmentUserRepos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentUserRepos.setAdapter(adapter);

        return binding.getRoot();
    }

    private void setUser(final User user) {
        this.user = user;
        if (binding != null) {
            Glide.with(getContext()).load(user.getAvatarUrl()).into(binding.fragmentUserAvatar);
            Glide.with(getContext()).load(user.getAvatarUrl()).into(binding.fragmentUserAvatarSmall);

            binding.fragmentUserLogin.setText(user.getLogin());
            binding.fragmentUserLoginSmall.setText(user.getLogin());

            if (user.getName() != null) {
                binding.fragmentUserName.setText(user.getName());
                binding.fragmentUserName.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleError(final Throwable throwable) {
        throwable.printStackTrace();
        if (binding != null) {
            Snackbar.make(binding.getRoot(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
    }
}
