package ru.nobird.github.search.api;


import java.util.List;

import ru.nobird.github.search.data.model.SearchItem;

public class UserSearchResponse {
    private long total_count;
    private boolean incomplete_results;
    private List<SearchItem> items;

    public List<SearchItem> getItems() {
        return items;
    }
}
