package please.hire.me.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import please.hire.me.R;
import please.hire.me.databinding.ProductDetailBinding;
import please.hire.me.model.ProductModel;

public class ProductDetailActivity extends AppCompatActivity {

    ProductDetailBinding viewBinding;
    ProductModel productModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.product_detail);
        viewBinding.setContext(this);

        String pojo = getIntent().getStringExtra("json");
        productModel = new Gson().fromJson(pojo, ProductModel.class);

        setSupportActionBar(viewBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadContent(productModel);
    }

    private void loadContent(ProductModel productModel) {

        getSupportActionBar().setTitle(productModel.getTitle());
        viewBinding.subtitle.setText(productModel.getDescription());
        viewBinding.star.setText(String.valueOf(productModel.getStarCount()));
        viewBinding.distance.setText("0");
        viewBinding.voucher.setText("voucher");

        Glide.with(this).load(productModel.getImageUrl()).into(viewBinding.image);
    }
}
