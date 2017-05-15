package ru.nobird.github.search.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.nobird.github.search.R;
import ru.nobird.github.search.api.API;
import ru.nobird.github.search.data.db.DBMgr;
import ru.nobird.github.search.data.model.UserSearchItem;
import ru.nobird.github.search.data.model.User;
import ru.nobird.github.search.databinding.FragmentUserBinding;
import ru.nobird.github.search.ui.adapter.RepoItemsAdapter;
import ru.nobird.github.search.ui.helper.UIHelper;

public class UserFragment extends Fragment {
    private static final String ARG_SEARCH_ITEM = "user_search_item";
    private static final String ARG_USER_ITEM = "user_item";

    public static Fragment newInstance(final UserSearchItem item) {
        return newInstance(ARG_SEARCH_ITEM, item);
    }

    public static Fragment newInstance(final User item) {
        return newInstance(ARG_USER_ITEM, item);
    }

    public static Fragment newInstance(final String tag, final Parcelable parcelable) {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(tag, parcelable);
        final Fragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private CompositeDisposable compositeDisposable;
    private UserSearchItem item;
    private User user;
    private FragmentUserBinding binding;

    private RepoItemsAdapter adapter;

    private MenuItem add, remove;

    private enum BookState {
        Booked, NotBooked, Undefined
    }

    private BookState bookState = BookState.Undefined;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        compositeDisposable = new CompositeDisposable();
        adapter = new RepoItemsAdapter();

        String login = null;

        item = getArguments().getParcelable(ARG_SEARCH_ITEM);
        if (item != null) {
            compositeDisposable.add(API.getInstance()
                    .getUser(item.getLogin())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setUser, this::handleError));

            login = item.getLogin();
        }

        setUser(getArguments().getParcelable(ARG_USER_ITEM));
        if (user != null) {
            login = user.getLogin();
        }

        if (login != null) {
            compositeDisposable.add(API.getInstance()
                    .getUserRepos(login)
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_menu, menu);

        add = menu.findItem(R.id.user_menu_add);
        remove = menu.findItem(R.id.user_menu_remove);

        setBookState(bookState);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (user != null) {
            switch (item.getItemId()) {
                case R.id.user_menu_add:
                    setBookState(BookState.Undefined);
                    compositeDisposable.add(
                            Completable.fromAction(() -> DBMgr.getInstance().addToBookmarks(user))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> setBookState(BookState.Booked))
                    );
                break;
                case R.id.user_menu_remove:
                    setBookState(BookState.Undefined);
                    compositeDisposable.add(
                            Completable.fromAction(() -> DBMgr.getInstance().removeFromBookmarks(user))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> setBookState(BookState.NotBooked))
                    );
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUser(final User user) {
        if (user == null) return;
        if (this.user != user) {
            compositeDisposable.add(Observable.fromCallable(() -> DBMgr.getInstance().isInBookmarks(user))
                    .map(b -> b ? BookState.Booked : BookState.NotBooked)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setBookState)
            );
        }
        this.user = user;

        if (binding != null) {
            Glide.with(getContext()).load(user.getAvatarUrl()).into(binding.fragmentUserAvatar);
            Glide.with(getContext()).load(user.getAvatarUrl()).into(binding.fragmentUserAvatarSmall);

            binding.fragmentUserLogin.setText(user.getLogin());
            binding.fragmentUserLoginSmall.setText(user.getLogin());

            UIHelper.setTextOrHideIfNull(binding.fragmentUserName, user.getName());
        }
    }

    private void setBookState(final BookState state) {
        this.bookState = state;
        if (add != null && remove != null) {
            switch (state) {
                case Booked:
                    add.setVisible(false);
                    remove.setVisible(true);
                    break;
                case NotBooked:
                    add.setVisible(true);
                    remove.setVisible(false);
                    break;
                case Undefined:
                    add.setVisible(false);
                    remove.setVisible(false);
                    break;
            }
        }
    }

    private void handleError(final Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
        }

        if (binding != null) {
            Snackbar.make(binding.getRoot(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
    }

}
