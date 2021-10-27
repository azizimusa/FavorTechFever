package please.hire.me;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.appspector.sdk.AppSpector;

import please.hire.me.database.AppDatabase;
import timber.log.Timber;

public class MyApp extends Application {

    private static MyApp instance;

    public static synchronized MyApp getInstance() {
        return instance;
    }

    private Retropow retropow;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AppSpector
                .build(this)
                .addHttpMonitor()
//                .addScreenshotMonitor()
                .addFileSystemMonitor()
                .addSharedPreferenceMonitor()
                .addSQLMonitor()
                .run(Constant.SECRET_KEY);
    }

    public Retropow getRestApi() {

        if (retropow == null) {
            retropow = Retropow.Factory.create();
        }

        return retropow;
    }

    public SharedPreferences getSharedPrefs() {
        return getSharedPreferences("favor", Context.MODE_PRIVATE);
    }
}
