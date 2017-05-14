package ru.nobird.github.search.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nobird.github.search.data.model.Repo;
import ru.nobird.github.search.data.model.User;

public class API {

    private static final String HOST = "https://api.github.com/";

    public static final long PER_PAGE = 100;

    private static final long REPOS_PER_PAGE = 6;
    private static final String REPOS_SORT = "updated";

    private final GitHubService gitHubService;

    private API() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()).build();

        this.gitHubService = retrofit.create(GitHubService.class);
    }

    private static API instance;

    public synchronized static void init() {
        if (instance == null) {
            instance = new API();
        }
    }

    public synchronized static API getInstance() {
        return instance;
    }

    public Observable<UserSearchResponse> searchUsers(final String query, final long page) {
        return gitHubService.searchUsers(query, page, PER_PAGE);
    }

    public Observable<User> getUser(final String username) {
        return gitHubService.getUser(username);
    }

    public Observable<List<Repo>> getUserRepos(final String username) {
        return gitHubService.getUserRepos(username, REPOS_SORT, REPOS_PER_PAGE);
    }
}
