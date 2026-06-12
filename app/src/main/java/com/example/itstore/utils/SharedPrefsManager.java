package com.example.itstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class SharedPrefsManager {
    private static final String PREF_NAME = "ITStorePrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static SharedPrefsManager instance;
    private final SharedPreferences sharedPreferences;
    private static final String KEY_SEARCH_HISTORY = "search_history";
    private static final int MAX_HISTORY = 10;

    private SharedPrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveTokens(String accessToken, String refreshToken) {
        sharedPreferences.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public void saveUserInfo(int id, String fullName, String email,  String role) {
        sharedPreferences.edit().putInt("user_id", id)
        .putString("user_fullname", fullName)
        .putString("user_email", email)
        .putString("user_role", role)
        .apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("user_id", -1);
    }

    public String getUserFullName() {
        return sharedPreferences.getString("user_fullname", "Đang tải...");
    }

    public String getUserEmail() {
        return sharedPreferences.getString("user_email", "Đang tải...");
    }



    public String getUserRole() {
        return sharedPreferences.getString("user_role", "Guest");
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }
    public List<String> getSearchHistory() {
        String raw = sharedPreferences.getString(KEY_SEARCH_HISTORY, "");
        if (raw.isEmpty()) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>(java.util.Arrays.asList(raw.split("\\|\\|")));
    }

    public void addSearchHistory(String keyword) {
        List<String> list = getSearchHistory();
        list.remove(keyword);
        list.add(0, keyword);
        if (list.size() > MAX_HISTORY) list = list.subList(0, MAX_HISTORY);
        sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, android.text.TextUtils.join("||", list)).apply();
    }

    public void removeSearchHistory(String keyword) {
        List<String> list = getSearchHistory();
        list.remove(keyword);
        sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, android.text.TextUtils.join("||", list)).apply();
    }
    public void clearSearchHistory() {
        sharedPreferences.edit().remove(KEY_SEARCH_HISTORY).apply();
    }
    public void clear() { sharedPreferences.edit().clear().apply(); }
}
