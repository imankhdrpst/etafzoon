package com.mindology.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.TokenResponse;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefrencesHelper {
    //    private static final String KEY_CREATING_INSPECTION = "creating_inspection";
    private static final String KEY_LIST_INSPECTION = "inspection_list_drafts";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_REFRESH = "refresh_token";
    public static final String KEY_REFRESH_UNTIL = "refresh_valid_until";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_TOKEN_SCOPE = "token_scope";
    public static final String KEY_VALID_UNTIL = "valid_until";

    private static SharedPreferences sharedPreferences;
    public static SharedPrefrencesHelper instance = null;

    public static synchronized SharedPrefrencesHelper getInstance(Application applicatin) {
        if (instance == null) {
            instance = new SharedPrefrencesHelper();
            sharedPreferences = applicatin.getSharedPreferences("yas-inv", Context.MODE_PRIVATE);
        }
        return instance;
    }

    public static synchronized Inspection getPersistedInspectionById(String id) {
//        if (TextUtils.isEmpty(id)) {
//            return getPersistedCreatingNewInspection();
//        }
        for (Inspection item : getPersistedInspectionList()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static synchronized List<Inspection> getPersistedInspectionList() {
        String str = sharedPreferences.getString(KEY_LIST_INSPECTION, "");
        List<Inspection> list = (new Gson()).fromJson(str, new TypeToken<List<Inspection>>() {}.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static synchronized void addInspectionToDrafts(Inspection inspection) {
        if (TextUtils.isEmpty(inspection.getId())) {
//            persistNewCreatingInspection(inspection);
            return;
        }
        List<Inspection> list = getPersistedInspectionList();
        for (Inspection item : list) {
            if (item.getId().equals(inspection.getId())) {
                list.remove(item);
                list.add(inspection);
                persistInspectionList(list);
                return;
            }
        }
        list.add(inspection);
        persistInspectionList(list);
    }

    public static synchronized void removeInspectionFromPersistedList(Inspection inspection) {
        if (inspection == null) {
//            persistNewCreatingInspection(null);
            return;
        }
        List<Inspection> list = getPersistedInspectionList();
        for (Inspection item : list) {
            if (item.getId().equals(inspection.getId())) {
                list.remove(item);
            }
        }
        persistInspectionList(list);
    }

    private static synchronized void persistInspectionList(List<Inspection> persistedInspectionList) {
        sharedPreferences.edit().putString(KEY_LIST_INSPECTION, (new Gson()).toJson(persistedInspectionList)).commit();
    }

    public static synchronized String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public static synchronized void clearTokenOnPrefs() {
        sharedPreferences.edit().putString(KEY_TOKEN, null)
                .putLong(KEY_VALID_UNTIL, 0)
                .putString(KEY_TOKEN_SCOPE, null)
                .putString(KEY_TOKEN_TYPE, null)
                .apply();
    }

    public static synchronized void saveToken(TokenResponse token) {
        sharedPreferences.edit().putString(KEY_TOKEN, token.getToken()).apply();
    }

    public static synchronized void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    //    private static synchronized void persistNewCreatingInspection(Inspection inspection) {
//        sharedPreferences.edit().putString(KEY_CREATING_INSPECTION, inspection == null ? "" : (new Gson()).toJson(inspection)).commit();
//    }
//
//    private static synchronized Inspection getPersistedCreatingNewInspection() {
//        String modelStr = sharedPreferences.getString(KEY_CREATING_INSPECTION, "");
//        if (TextUtils.isEmpty(modelStr)) {
//            return null;
//        }
//        return (new Gson()).fromJson(modelStr, Inspection.class);
//    }
//
    public void clearAll() {
//        sharedPreferences.edit().putString(KEY_CREATING_INSPECTION,"").commit();
        sharedPreferences.edit().putString(KEY_LIST_INSPECTION,"").commit();
    }
}
