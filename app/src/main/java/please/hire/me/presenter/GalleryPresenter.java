package please.hire.me.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.marchinram.rxgallery.RxGallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import please.hire.me.Constant;
import please.hire.me.R;
import please.hire.me.UriHelper;
import please.hire.me.Util;
import please.hire.me.view.GalleryActivity;
import timber.log.Timber;

public class GalleryPresenter {

    private final Action action;
    private final Context context;
    private Disposable disposable;

    public interface Action {
        void resultGallery(List<File> files);
    }

    public GalleryPresenter(Action action, Context context, Disposable disposable) {
        this.action = action;
        this.context = context;
        this.disposable = disposable;
    }

    public void pickFromGallery() {

        disposable = RxGallery.gallery((GalleryActivity) context, true, RxGallery.MimeType.VIDEO).subscribe(new Consumer<List<Uri>>() {
            @Override
            public void accept(List<Uri> uris) throws Exception {
                StringBuffer message = new StringBuffer(context.getResources()
                        .getQuantityString(R.plurals.selected_items, uris.size(), uris.size()));

                List<File> files = new ArrayList<>();

                for (Uri uri : uris) {
                    String g = UriHelper.getInstance().getPathUsingContentResolver(uri, context).split(":")[1];
                    String pp = Environment.getExternalStorageDirectory() + File.separator + g;

                    files.add(new File(pp));
                }

                action.resultGallery(files);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
