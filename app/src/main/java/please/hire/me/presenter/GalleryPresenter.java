package please.hire.me.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.marchinram.rxgallery.RxGallery;

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
        void resultGallery(Uri uri);

    }

    public GalleryPresenter(Action action, Context context, Disposable disposable) {
        this.action = action;
        this.context = context;
        this.disposable = disposable;
    }

    private long getFileSize(Uri fileUri) {
        Cursor returnCursor = context.getContentResolver().
                query(fileUri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        long result = returnCursor.getLong(sizeIndex);
        returnCursor.close();

        return result;
    }

    public void pickFromGallery(View view) {

        disposable = RxGallery.gallery((GalleryActivity) context, false, RxGallery.MimeType.VIDEO).subscribe(new Consumer<List<Uri>>() {
            @Override
            public void accept(List<Uri> uris) throws Exception {
                StringBuffer message = new StringBuffer(context.getResources()
                        .getQuantityString(R.plurals.selected_items, uris.size(), uris.size()));
                for (Uri uri : uris) {
                    message.append("\n").append(uri.toString());
                }

                action.resultGallery(uris.get(0));

                long sizeByte = getFileSize(uris.get(0));

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
