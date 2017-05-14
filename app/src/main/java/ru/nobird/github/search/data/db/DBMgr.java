package ru.nobird.github.search.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.nobird.github.search.data.model.User;

public class DBMgr {

    private static DBMgr instance;
    private final SQLiteDatabase db;

    private DBMgr(final Context context) {
        db = new DBHelper(context).getWritableDatabase();
    }

    public synchronized static void init(final Context context) {
        if (instance == null) {
            instance = new DBMgr(context);
        }
    }

    public synchronized static DBMgr getInstance() {
        return instance;
    }

    public void addToBookmarks(final User user) {
        final ContentValues cv = new ContentValues();
        cv.put("id", user.getId());
        cv.put("login", user.getLogin());
        cv.put("name", user.getName());
        cv.put("avatar_url", user.getAvatarUrl());
        cv.put("html_url", user.getHtml_url());
        cv.put("location", user.getLocation());
        cv.put("bio", user.getBio());
        cv.put("followers", user.getFollowers());
        cv.put("following", user.getFollowing());
        db.insert(DBHelper.TABLE_BOOKMARKS, null, cv);
    }

    public boolean isExistWithID(final String table, final String fieldName, final long id) {
        final Cursor cursor = db.query(table, null, fieldName + " = " + id, null, null, null, "id DESC");
        final boolean r = cursor.moveToFirst();
        cursor.close();
        return r;
    }

    public boolean isInBookmarks(final User user) {
        return isExistWithID(DBHelper.TABLE_BOOKMARKS, "id", user.getId());
    }

    public void removeFromBookmarks(final User user) {
        removeElementFromTableByID(DBHelper.TABLE_BOOKMARKS, "id", user.getId());
    }

    public void removeElementFromTableByID(final String table, final String fieldName, final long id){
        db.delete(table, fieldName + " = " + id, null);
    }

    public List<User> getBookmarks() {
        final Cursor cursor = db.query(DBHelper.TABLE_BOOKMARKS, null, null, null, null, null, "id DESC");
        final ArrayList<User> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                items.add(new User(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("login")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("avatar_url")),
                        cursor.getString(cursor.getColumnIndex("html_url")),
                        cursor.getString(cursor.getColumnIndex("location")),
                        cursor.getString(cursor.getColumnIndex("bio")),
                        cursor.getLong(cursor.getColumnIndex("followers")),
                        cursor.getLong(cursor.getColumnIndex("following"))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }
}
