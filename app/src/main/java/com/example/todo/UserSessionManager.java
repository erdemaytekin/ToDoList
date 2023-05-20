package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String userID = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Kullanıcı ID'sini kaydet
    public void saveUserId(int userId) {
        editor.putInt("USER_ID", userId);
        editor.commit();
    }

    // Kaydedilmiş Kullanıcı ID'sini getir
    public int getUserId() {
        return sharedPreferences.getInt("USER_ID", -1); // -1: Varsayılan değer, kullanıcı ID'si kaydedilmemişse
    }

    // Kullanıcı oturumunu sonlandır ve kayıtlı Kullanıcı ID'sini sil
    public void logout() {
        editor.remove("USER_ID");
        editor.commit();
    }

}