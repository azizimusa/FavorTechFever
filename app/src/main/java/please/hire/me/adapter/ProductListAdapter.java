package please.hire.me.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.List;

import please.hire.me.R;
import please.hire.me.databinding.RvRowBinding;
import please.hire.me.model.ProductModel;
import timber.log.Timber;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> productModels;
    private OnProductList onProductList;

    public interface OnProductList {
        void updateProductCacheImageUrl(ProductModel productModel);
        void onSelectProduct(ProductModel productModel);
    }

    public ProductListAdapter(Context context, List<ProductModel> productModels, OnProductList onProductList) {
        this.context = context;
        this.productModels = productModels;
        this.onProductList = onProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductModel model = productModels.get(position);

        holder.view.title.setText(model.getTitle());
        holder.view.price.setText("RM " + model.getPrice());

        String getCachedImage = context.getCacheDir().getAbsolutePath() + File.separator + "images" + File.separator + model.getImageUrl().split("/")[4];
        Bitmap cachedBitmap = BitmapFactory.decodeFile(getCachedImage, null);
        Drawable cachedDrawable = new BitmapDrawable(context.getResources(), cachedBitmap);

        if (cachedDrawable != null) {
            Glide.with(context).load(model.getImageUrl()).fallback(cachedDrawable).into(holder.view.iv);
        } else {
            Glide.with(context).load(model.getImageUrl()).into(holder.view.iv);
        }

        Glide.with(context)
                .load(holder.view.iv.getDrawable())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        savebitmap(model.getImageUrl().split("/")[4], bitmap);

//                        model.setImageCacheUrl(savedBitmap.getAbsolutePath());
//                        onProductList.updateProductCacheImageUrl(model);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        holder.view.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductList.onSelectProduct(model);
            }
        });

    }

    private File savebitmap(String filename, Bitmap bitmap) {
        String getAppFolder = context.getCacheDir().getAbsolutePath() + File.separator + "images";

        new File(getAppFolder).mkdir();

        File file = new File(getAppFolder, filename);

        OutputStream outStream;

        if (file.exists()) {
            file.delete();
            file = new File(getAppFolder, filename);
        }

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public void update(List<ProductModel> productModels) {
        productModels.clear();
        productModels.addAll(productModels);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RvRowBinding view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = DataBindingUtil.bind(itemView);
        }
    }
}
