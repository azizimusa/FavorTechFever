package please.hire.me.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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

        getSupportActionBar().setTitle(productModel.getCategory().toUpperCase());
        viewBinding.title.setText(productModel.getTitle());
        viewBinding.subtitle.setText(productModel.getDescription());
        viewBinding.star.setText("RM " + productModel.getPrice());
        viewBinding.distance.setText("Rating : " + productModel.getRating());
        viewBinding.voucher.setText("Liked : " + productModel.getStarCount());

        Glide.with(this).load(productModel.getImageUrl()).into(viewBinding.image);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                onBackPressed();

                // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
                break;

        }

        return true;
    }
}
