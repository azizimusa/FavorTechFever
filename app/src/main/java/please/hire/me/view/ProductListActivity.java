package please.hire.me.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import please.hire.me.MyApp;
import please.hire.me.R;
import please.hire.me.Util;
import please.hire.me.adapter.ProductListAdapter;
import please.hire.me.databinding.ProductListBinding;
import please.hire.me.model.ProductModel;
import please.hire.me.model.StoreModel;
import please.hire.me.presenter.ProductListPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ProductListActivity extends AppCompatActivity implements ProductListPresenter.Action {
    ProductListBinding viewBinding;
    ProductListPresenter presenter;
    ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.product_list);
        viewBinding.setContext(this);

        presenter = new ProductListPresenter(this, this);

        getProducts();
    }

    private void initListing(List<ProductModel> productModels) {
        adapter = new ProductListAdapter(ProductListActivity.this, productModels, new ProductListAdapter.OnProductList() {
            @Override
            public void updateProductCacheImageUrl(ProductModel productModel) {
                Timber.e(new Gson().toJson(productModel));
                presenter.updateRecord(productModel);
            }

            @Override
            public void onSelectProduct(ProductModel productModel) {
                String json = new Gson().toJson(productModel);
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("json", json);
                startActivity(intent);
            }
        });
        viewBinding.rv.setAdapter(adapter);
        viewBinding.rv.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
    }

    private void getProducts() {
        Call<ResponseBody> call = MyApp.getInstance().getRestApi().getProducts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    try {
                        String json = response.body().string();

                        Type collectionType = new TypeToken<Collection<StoreModel>>(){}.getType();
                        Collection<StoreModel> storeModels = new Gson().fromJson(json, collectionType);

                        List<ProductModel> productModels = new ArrayList<>();

                        for (StoreModel a : storeModels) {

                            String getCachedImage = getCacheDir().getAbsolutePath() + "/images/" + a.getImage().split("/")[4];

                            ProductModel model = new ProductModel();
                            model.setCategory(a.getCategory());
                            model.setDescription(a.getDescription());
                            model.setImageUrl(a.getImage());
                            model.setImageCacheUrl(getCachedImage);
                            model.setPrice(a.getPrice());
                            model.setRating(a.getRating().getRate());
                            model.setStarCount(a.getRating().getCount());
                            model.setTitle(a.getTitle());

                            productModels.add(model);
                        }

                        initListing(productModels);

                        presenter.save(productModels);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                presenter.runOfflineMode();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void update(ProductModel productModel, String msg) {
        Util.showToast(this, msg);
    }

    @Override
    public void offlineMode(List<ProductModel> productModels) {

        Timber.e("offline haha " + productModels.size());
        initListing(productModels);
    }
}
