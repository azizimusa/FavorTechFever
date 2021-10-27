package please.hire.me.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import please.hire.me.R;
import please.hire.me.Util;
import please.hire.me.databinding.LoginBinding;
import please.hire.me.model.User;
import please.hire.me.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.Action {

    LoginBinding viewBinding;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.login);
        viewBinding.setContext(this);

        loginPresenter = new LoginPresenter(this, this);

    }

    public void login() {

        if (!TextUtils.isEmpty(viewBinding.username.getText()) && !TextUtils.isEmpty(viewBinding.password.getText())) {
            loginPresenter.doLogin(viewBinding.username.getText().toString(), viewBinding.password.getText().toString());
        } else {
            Util.showToast(this, "Please fill in all fields");
        }

    }

    public void openSignUpPage() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void update(boolean result, User user, String msg) {

        Util.showToast(this, msg);

        if (result) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.username.setText(Util.getCurrentUsername());
    }
}
