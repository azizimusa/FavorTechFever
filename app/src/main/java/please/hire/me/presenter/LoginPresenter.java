package please.hire.me.presenter;

import android.content.Context;
import android.view.View;

import please.hire.me.Util;
import please.hire.me.database.AppDatabase;
import please.hire.me.database.AppExecutors;
import please.hire.me.model.User;

public class LoginPresenter {

    private Action action;
    private final Context context;
    AppDatabase appDatabase;

    public interface Action {
        void update(boolean result, User user, String msg);
    }

    public LoginPresenter(Action action, Context context) {
        this.action = action;
        this.context = context;

        appDatabase = AppDatabase.getInstance(context);
    }

    private void updater(boolean result, User user, String msg) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                Util.isLogin(result);

                if (result) {
                    Util.setCurrentUsername(user.getUsername());
                    action.update(true, user, msg);
                } else {
                    action.update(false, null, msg);
                }
            }
        });
    }

    public void doLogin(String username, String password) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    if (appDatabase.userDao().checkUsername(username)) {
                        User user = appDatabase.userDao().selectUser(username, password);

                        if (user != null) {
                            updater(true, user, "Welcome " + user.getName());
                        } else {
                            updater(false, null, "You have enter a wrong password");
                        }

                    } else {
                        updater(false, null, "User not exist");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
