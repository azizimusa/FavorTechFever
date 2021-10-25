package please.hire.me;

import android.app.Application;

import com.appspector.sdk.AppSpector;

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
                .addScreenshotMonitor()
                .addSQLMonitor()
                .run(Constant.SECRET_KEY);
    }

    public Retropow getRestApi() {

        if (retropow == null) {
            retropow = Retropow.Factory.create();
        }

        return retropow;
    }
}
