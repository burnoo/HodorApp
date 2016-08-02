package pl.droidsonroids.hodor;

import android.app.Application;

import pl.droidsonroids.hodor.retrofit.RestAdapter;
import pl.droidsonroids.hodor.util.DatabaseHelper;

public class HodorApplication extends Application {

    private static HodorApplication sInstance;

    private HodorPreferences mHodorPreferences;
    private RestAdapter mRestAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mHodorPreferences = new HodorPreferences(this);
        mRestAdapter = new RestAdapter();
        mDatabaseHelper = new DatabaseHelper();
    }

    public static HodorApplication getInstance() {
        return sInstance;
    }

    public HodorPreferences getPreferences() {
        return mHodorPreferences;
    }

    public RestAdapter getRestAdapter() {
        return mRestAdapter;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }
}
