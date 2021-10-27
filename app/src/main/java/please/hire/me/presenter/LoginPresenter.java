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
        void update(boolean result, User user);
    }

    public LoginPresenter(Action action, Context context) {
        this.action = action;
        this.context = context;

        appDatabase = AppDatabase.getInstance(context);
    }

    public void doLogin(String username, String password) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = appDatabase.userDao().selectUser(username, password);

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            action.update(user != null, user);
                            Util.isLogin(user != null);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
