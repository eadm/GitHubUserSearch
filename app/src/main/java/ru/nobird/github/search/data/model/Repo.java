package ru.nobird.github.search.data.model;

public class Repo {
    private long id;
    private String name;
    private String full_name;
    private String html_url;
    private String description;
    private long stargazers_count;
    private long watchers_count;
    private String language;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getDescription() {
        return description;
    }

    public long getStargazers_count() {
        return stargazers_count;
    }

    public long getWatchers_count() {
        return watchers_count;
    }

    public String getLanguage() {
        return language;
    }
}
