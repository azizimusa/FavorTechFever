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
}
