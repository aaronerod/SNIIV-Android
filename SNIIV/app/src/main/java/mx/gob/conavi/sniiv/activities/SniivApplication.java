package mx.gob.conavi.sniiv.activities;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by admin on 11/08/15.
 */
public class SniivApplication extends Application {
    private static SniivApplication singleton;
    private static final String PREFS_NAME = "MyPrefsFile";

    public static SniivApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public void setTimeLastUpdated(String key,long time) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, time);

        editor.commit();
    }

    public long getTimeLastUpdated(String key) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getLong(key, 0);
    }
}
