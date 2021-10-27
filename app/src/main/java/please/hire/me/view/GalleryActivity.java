package please.hire.me.view;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;
import please.hire.me.R;
import please.hire.me.Util;
import please.hire.me.adapter.VideoGalleryAdapter;
import please.hire.me.databinding.GalleryBinding;
import please.hire.me.presenter.GalleryPresenter;
import timber.log.Timber;
public class GalleryActivity extends AppCompatActivity implements GalleryPresenter.Action{
    GalleryBinding viewBinding;
    GalleryPresenter presenter;
    private Disposable disposable;
    VideoGalleryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = DataBindingUtil.setContentView(this, R.layout.gallery);
        viewBinding.setContext(this);
        presenter = new GalleryPresenter(this, this, disposable);
    }

    public void onFab() {

        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted ->{
                    if (granted) {
                        presenter.pickFromGallery();
                    } else {
                        Util.showToast(GalleryActivity.this, "You need to allow permission to use this feature");
                    }
                });
    }

    private void initListing(List<File> fileList) {

        adapter = new VideoGalleryAdapter(this, fileList, new VideoGalleryAdapter.OnVideoGallery() {
            @Override
            public void onSelect(File file) {
                Timber.e("Select " + file.getName());
            }

            @Override
            public void onCompress(File file) {
                Timber.e("Compress " + file.getName());
            }
        });

        viewBinding.rv.setAdapter(adapter);
        viewBinding.rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void resultGallery(List<File> files) {
        initListing(files);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
