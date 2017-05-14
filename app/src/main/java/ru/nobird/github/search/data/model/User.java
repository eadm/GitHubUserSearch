package ru.nobird.github.search.data.model;

public class User {
    private String login;
    private long id;
    private String avatarUrl;
    private String gravatarId;
    private String url;
    private String htmlUrl;
    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String starredUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String eventsUrl;
    private String receivedEventsUrl;
    private String type;
    private boolean siteAdmin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private boolean hireable;
    private String bio;
    private long privateRepos;
    private long privateGists;
    private long followers;
    private long following;
    private String createdAt;
    private String updatedAt;

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getBio() {
        return bio;
    }

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }
}
