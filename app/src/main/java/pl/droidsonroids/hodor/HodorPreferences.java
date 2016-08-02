package pl.droidsonroids.hodor;

import android.content.Context;
import android.content.SharedPreferences;

public class HodorPreferences {

    private SharedPreferences mAppSharedPreferences;

    public HodorPreferences(Context context) {
        mAppSharedPreferences = context.getSharedPreferences(Constants.PREF, 0);
    }

    public void setUsername(String username) {
        mAppSharedPreferences.edit().putString(Constants.USERNAME_KEY, username).apply();
    }

    public String getUsername() {
        return mAppSharedPreferences.getString(Constants.USERNAME_KEY, null);
    }
}
