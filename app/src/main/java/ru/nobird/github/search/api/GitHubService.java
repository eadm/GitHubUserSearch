package ru.nobird.github.search.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nobird.github.search.data.model.Repo;
import ru.nobird.github.search.data.model.User;

public interface GitHubService {


    @GET("search/users")
    Observable<UserSearchResponse> searchUsers(
            @Query("q") final String query,
            @Query("page") final long page,
            @Query("per_page") final long per_page
    );

    @GET("users/{username}")
    Observable<User> getUser(
            @Path("username") final String username
    );

    @GET("users/{username}/repos")
    Observable<List<Repo>> getUserRepos(
            @Path("username") final String username,
            @Query("sort") final String sort,
            @Query("per_page") final long per_page
    );

}
