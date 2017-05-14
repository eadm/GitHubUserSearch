package ru.nobird.github.search.data.model;

public class SearchItem {
    private String login;
    private long id;
    private String avatarUrl;
    private String gravatarId;
    private String url;
    private String htmlUrl;
    private String followersUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String receivedEventsUrl;
    private String type;
    private double score;

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
