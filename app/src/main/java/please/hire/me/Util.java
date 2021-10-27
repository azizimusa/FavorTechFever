package please.hire.me;

import android.content.Context;
import android.widget.Toast;

public class Util {
    private static Toast toast;

    public static void showToast(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void isLogin(boolean value) {
        MyApp.getInstance().getSharedPrefs().edit().putBoolean(Constant.IS_LOGIN, value).apply();
    }

    public static boolean isLogin() {
        return MyApp.getInstance().getSharedPrefs().getBoolean(Constant.IS_LOGIN, false);
    }

    public static void setCurrentUsername(String value) {
        MyApp.getInstance().getSharedPrefs().edit().putString(Constant.USERNAME, value).apply();
    }

    public static String getCurrentUsername() {
        return MyApp.getInstance().getSharedPrefs().getString(Constant.USERNAME, "false");
    }
}
