package please.hire.me.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import please.hire.me.R;
import please.hire.me.Util;
import please.hire.me.databinding.SignupBinding;
import please.hire.me.presenter.SignUpPresenter;
import timber.log.Timber;

public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.Action {

    SignupBinding viewBinding;
    SignUpPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.signup);
        viewBinding.setContext(this);

        presenter = new SignUpPresenter(this, this);

    }

    // TODO: 26/10/2021 validate input
    public void signup() {

        if (!TextUtils.isEmpty(viewBinding.name.getText()) && !TextUtils.isEmpty(viewBinding.username.getText()) && !TextUtils.isEmpty(viewBinding.password.getText()) && !TextUtils.isEmpty(viewBinding.nric.getText()) && !TextUtils.isEmpty(viewBinding.phone.getText())) {

            presenter.createNewUser(viewBinding.name.getText().toString(), viewBinding.username.getText().toString(), viewBinding.password.getText().toString(), viewBinding.nric.getText().toString(), viewBinding.phone.getText().toString());

        } else {
            Util.showToast(this, "Please complete the form");
        }

    }

    @Override
    public void update(String content) {
        Util.showToast(this, content);
    }

}
