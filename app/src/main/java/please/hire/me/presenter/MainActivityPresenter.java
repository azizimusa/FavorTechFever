package please.hire.me.presenter;

import android.content.Context;

public class MainActivityPresenter {

    private final Action action;
    private final Context context;

    public interface Action {
    }

    public MainActivityPresenter(Action action, Context context) {
        this.action = action;
        this.context = context;
    }
}
