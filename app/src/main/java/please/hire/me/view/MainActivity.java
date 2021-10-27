package please.hire.me.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import please.hire.me.R;
import please.hire.me.databinding.ActivityMainBinding;
import please.hire.me.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.Action {
    ActivityMainBinding viewBinding;
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewBinding.setContext(this);

        presenter = new MainActivityPresenter(this, this);

    }

    public void productList() {
        startActivity(new Intent(this, ProductListActivity.class));
    }

    public void gallery() {
        startActivity(new Intent(this, GalleryActivity.class));
    }

}