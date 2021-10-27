package please.hire.me.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import please.hire.me.Util;
import please.hire.me.database.AppDatabase;
import please.hire.me.database.AppExecutors;
import please.hire.me.database.ProductDao;
import please.hire.me.model.ProductModel;
import please.hire.me.model.StoreModel;
import timber.log.Timber;

public class ProductListPresenter {

    private final Action action;
    private final Context context;
    AppDatabase appDatabase;
    List<ProductModel> productModels = new ArrayList<>();

    public interface Action {
        void update(ProductModel productModel, String msg);
        void offlineMode(List<ProductModel> productModels);
    }

    public ProductListPresenter(Action action, Context context) {
        this.action = action;
        this.context = context;

        appDatabase = AppDatabase.getInstance(context);
    }

    public void updateRecord(ProductModel productModel) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    appDatabase.productDao().update(productModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<ProductModel> runOfflineMode() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    productModels = appDatabase.productDao().loadAllProduct();

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            action.offlineMode(productModels);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return productModels;
    }

    public void updater(boolean result, String msg) {

        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                action.update(null, msg);
            }
        });
    }

    public void save(List<ProductModel> productModels) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    appDatabase.productDao().deleteAll();
                    appDatabase.productDao().insert(productModels);
                    updater(true, "Save Success");
                } catch (Exception e) {
                    e.printStackTrace();
                    updater(true, "Error Occured");
                }
            }
        });

    }
}
