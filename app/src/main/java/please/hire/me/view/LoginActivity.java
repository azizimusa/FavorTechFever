package please.hire.me.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import please.hire.me.R;
import please.hire.me.databinding.LoginBinding;

public class LoginActivity extends AppCompatActivity {

    LoginBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.login);

    }
}
