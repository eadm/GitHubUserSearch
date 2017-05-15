package ru.nobird.github.search.api;


import java.util.List;

import ru.nobird.github.search.data.model.UserSearchItem;

public class UserSearchResponse {
    private long total_count;
    private boolean incomplete_results;
    private List<UserSearchItem> items;

    public List<UserSearchItem> getItems() {
        return items;
    }
}
