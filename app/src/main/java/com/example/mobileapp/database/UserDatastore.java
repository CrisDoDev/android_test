package com.example.mobileapp.database;

import android.content.Context;
import android.util.Log;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Flowable;

public class UserDatastore {
    private static final String DATASTORE_NAME = "user_prefs";
    private static RxDataStore<Preferences> dataStore;

    private static final Preferences.Key<Integer> ID_USER = PreferencesKeys.intKey("id_user");
    private static final Preferences.Key<String> FIREBASE_UID = PreferencesKeys.stringKey("firebaseUid");
    private static final Preferences.Key<String> FULL_NAME = PreferencesKeys.stringKey("fullName");
    private static final Preferences.Key<String> EMAIL = PreferencesKeys.stringKey("email");


    private static final Preferences.Key<String> PHONE_NUMBER = PreferencesKeys.stringKey("phoneNumber");

    private static final Preferences.Key<Integer> ROLE_ID = PreferencesKeys.intKey("role_id");
    private static final Preferences.Key<Boolean> IS_LOGGED_IN = PreferencesKeys.booleanKey("isLoggedIn");

    public UserDatastore(Context context) {
        if (dataStore == null) {
            dataStore = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), DATASTORE_NAME).build();
        }
    }

    //CẬP NHẬT: Nhận thêm tham số String phone để đóng gói khi Đăng nhập/Đăng ký thành công
    public void saveUser(int id, String uid, String name, String email, String phone, int role) {
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(ID_USER, id);
            mutablePreferences.set(FIREBASE_UID, uid);
            mutablePreferences.set(FULL_NAME, name);
            mutablePreferences.set(EMAIL, email);
            mutablePreferences.set(PHONE_NUMBER, phone != null ? phone : ""); // Lưu SĐT
            mutablePreferences.set(ROLE_ID, role);
            mutablePreferences.set(IS_LOGGED_IN, true);
            return Single.just(mutablePreferences);
        }).subscribe(
                prefs -> Log.d("UserDatastore", "Lưu thông tin đăng nhập thành công"),
                throwable -> Log.e("UserDatastore", "Lỗi không lưu được: " + throwable.getMessage())
        );
    }

    public Flowable<Integer> getRoleId() {
        return dataStore.data().map(prefs -> prefs.get(ROLE_ID) != null ? prefs.get(ROLE_ID) : -1);
    }

    public Flowable<Boolean> isLoggedIn() {
        return dataStore.data().map(prefs -> prefs.get(IS_LOGGED_IN) != null ? prefs.get(IS_LOGGED_IN) : false);
    }

    public void clear() {
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.clear();
            return Single.just(mutablePreferences);
        }).subscribe(
                prefs -> Log.d("UserDatastore", "Xóa session đăng nhập thành công"),
                throwable -> Log.e("UserDatastore", "Lỗi xóa session: " + throwable.getMessage())
        );
    }

    //  thêm thuộc tính SĐT vào đối tượng User chuyển về fragment
    public Flowable<com.example.mobileapp.model.User> getUser() {
        return dataStore.data().map(prefs -> {
            com.example.mobileapp.model.User user = new com.example.mobileapp.model.User();
            Integer id = prefs.get(ID_USER);
            user.setIdUser(id != null ? id : 0);
            user.setFirebaseUid(prefs.get(FIREBASE_UID));
            user.setFullName(prefs.get(FULL_NAME));
            user.setEmail(prefs.get(EMAIL));
            user.setPhoneNumber(prefs.get(PHONE_NUMBER));
            Integer role = prefs.get(ROLE_ID);
            user.setRoleId(role != null ? role : 0);
            return user;
        });
    }
}