package please.hire.me.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.marchinram.rxgallery.RxGallery;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import please.hire.me.Constant;
import please.hire.me.R;
import please.hire.me.UriHelper;
import please.hire.me.Util;
import timber.log.Timber;

public class GalleryPresenter {

    private final Action action;
    private final Context context;
    private Disposable disposable;

    public interface Action {
        void resultGallery(List<Uri> uri);

    }

    public GalleryPresenter(Action action, Context context, Disposable disposable) {
        this.action = action;
        this.context = context;
        this.disposable = disposable;
    }

    public void pickFromGallery(View view) {

        disposable = RxGallery.gallery((AppCompatActivity) context, true, RxGallery.MimeType.VIDEO).subscribe(new Consumer<List<Uri>>() {
            @Override
            public void accept(List<Uri> uris) throws Exception {
                StringBuffer message = new StringBuffer(context.getResources()
                        .getQuantityString(R.plurals.selected_items, uris.size(), uris.size()));
                for (Uri uri : uris) {
                    message.append("\n").append(uri.toString());
                }

                action.resultGallery(uris);

                long sizeByte = UriHelper.getInstance().getFileSize(uris.get(0), context);

//                String hehePath = UriHelper.getInstance().getPathUsingContentResolver(uris.get(0), context);
//                Timber.e(hehePath);

                if (sizeByte > Constant.TEN_MB_IN_BYTES) {
                    Util.showToast(context, "More than 10MB");
                } else {
                    Util.showToast(context, "Lesser than 10MB");
                }

                long sizeMB = sizeByte / (1024 * 1024);
                long roundOff = Math.round(sizeMB*100)/100;

                Timber.e(message.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
