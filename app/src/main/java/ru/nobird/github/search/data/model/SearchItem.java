package ru.nobird.github.search.data.model;

public class SearchItem {
    private String login;
    private long id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String followers_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String received_events_url;
    private String type;
    private double score;

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }
}
