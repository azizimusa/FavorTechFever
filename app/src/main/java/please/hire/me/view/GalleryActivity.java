package please.hire.me.view;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.File;

import io.reactivex.disposables.Disposable;
import please.hire.me.R;
import please.hire.me.UriHelper;
import please.hire.me.databinding.GalleryBinding;
import please.hire.me.presenter.GalleryPresenter;
import timber.log.Timber;

public class GalleryActivity extends AppCompatActivity implements GalleryPresenter.Action{
    GalleryBinding viewBinding;
    GalleryPresenter presenter;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.gallery);
        viewBinding.setContext(this);
        presenter = new GalleryPresenter(this, this, disposable);

    }

    public void onFab() {
        presenter.pickFromGallery(viewBinding.getRoot().getRootView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void resultGallery(Uri uri) {
        String path = UriHelper.getInstance().getPathUsingContentResolver(uri, this);
        Timber.e("terima di activity");
        Timber.e(path);

        String cutPath = path.split(":")[1];

        String pp = Environment.getExternalStorageDirectory() + File.separator + cutPath;
        File x = new File(pp);

        if (x.exists()) {
            Timber.e("hyahoooo exist");
            Timber.e("size nya ialah : " + x.length());
        }
    }
}
