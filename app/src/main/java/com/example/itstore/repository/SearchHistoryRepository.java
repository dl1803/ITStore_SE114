package com.example.itstore.repository;

import android.content.Context;
import com.example.itstore.utils.SharedPrefsManager;
import java.util.List;

public class SearchHistoryRepository {

    private static SearchHistoryRepository instance;
    private final SharedPrefsManager prefsManager;

    private SearchHistoryRepository(Context context) {
        this.prefsManager = SharedPrefsManager.getInstance(context.getApplicationContext());
    }

    public static synchronized SearchHistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SearchHistoryRepository(context.getApplicationContext());
        }
        return instance;
    }

    public List<String> getHistory() {
        return prefsManager.getSearchHistory();
    }

    public void addKeyword(String keyword) {
        prefsManager.addSearchHistory(keyword);
    }

    public void removeKeyword(String keyword) {
        prefsManager.removeSearchHistory(keyword);
    }

    public void clearAll() {
        prefsManager.clearSearchHistory();
    }
}