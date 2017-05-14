package ru.nobird.github.search.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public final static String TABLE_BOOKMARKS = "bookmarks";

    public DBHelper(final Context context) {
        super(context, "GitHubSearchDB", null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_BOOKMARKS + " ("
                + "id integer primary key autoincrement,"
                + "login text,"
                + "name text,"
                + "avatar_url text,"
                + "html_url text,"
                + "location text,"
                + "bio text,"
                + "followers integer,"
                + "following integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
