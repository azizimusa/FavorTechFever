package please.hire.me.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.google.gson.Gson;

import please.hire.me.MyApp;
import please.hire.me.Util;
import please.hire.me.database.AppDatabase;
import please.hire.me.database.AppExecutors;
import please.hire.me.model.User;
import please.hire.me.view.SignUpActivity;
import timber.log.Timber;

public class SignUpPresenter {

    private Action action;
    private Context context;
    private AppDatabase appDatabase;
    boolean isSuccess;

    public interface Action {
        void update(boolean result, String content);
    }

    public SignUpPresenter(Action action, Context context) {
        this.action = action;
        this.context = context;

        appDatabase = AppDatabase.getInstance(context);
    }

    private void updater(String msg, String username) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                action.update(username != null, msg);

                if (username != null) {
                    Util.setCurrentUsername(username);
                }
            }
        });
    }

    public void createNewUser(String name, String username, String password, String nric, String phone) {

        User user = new User(name, username, password, nric, phone);
        Timber.e(new Gson().toJson(user));
        isSuccess = false;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    if (appDatabase.userDao().checkRecordExist(username, nric, phone)) {
                        updater("Username, NRIC or Phone might already exist", null);
                    } else {
                        appDatabase.userDao().insertUser(user);
                        updater("Registration Success", username);
                    }

                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                    updater("Username, NRIC or Phone might already exist", null);
                }
            }
        });

    }
}
