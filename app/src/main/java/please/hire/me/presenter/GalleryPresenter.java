package please.hire.me.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.iamkdblue.videocompressor.VideoCompress;
import com.marchinram.rxgallery.RxGallery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import please.hire.me.R;
import please.hire.me.UriHelper;
import please.hire.me.Util;
import please.hire.me.view.GalleryActivity;
import timber.log.Timber;

public class GalleryPresenter {

    private final Action action;
    private final Context context;
    private Disposable disposable;
    ProgressDialog progressDialog;

    public interface Action {
        void resultGallery(List<File> files);
        void compressedDone(File file);
    }

    public GalleryPresenter(Action action, Context context, Disposable disposable) {
        this.action = action;
        this.context = context;
        this.disposable = disposable;
    }

    public void initProgressDialog(String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    public void compressVideo(File file) {

        String dest = Environment.getExternalStorageDirectory() + "/compressVideo";

        File newFile = new File(dest, "compress_"+file.getName());

        if (!new File(dest).exists()) {
            Timber.e("Creating " + dest + " : " + new File(dest).mkdir());
        }

        if (!newFile.exists()) {
            try {
                Timber.e("Creating " + newFile.getAbsolutePath() + " : " + newFile.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        VideoCompress.compressVideoLow(file.getAbsolutePath(), newFile.getAbsolutePath(), new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                Timber.e("onStart");
                initProgressDialog("Compressing " + file.getName());
                //Start Compress
            }

            @Override
            public void onSuccess(String compressVideoPath) {
                Timber.e("onSuccess : " + compressVideoPath);
                progressDialog.dismiss();
                action.compressedDone(new File(compressVideoPath));
                //Finish successfully
            }

            @Override
            public void onFail() {
                progressDialog.dismiss();
                Util.showToast(context, "Compress Failed");
                Timber.e("onFail");
                //Failed
            }

            @Override
            public void onProgress(float percent) {
                progressDialog.setProgress((int) percent);
                //Progress
            }
        });

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
                    String pp = Environment.getExternalStorageDirectory() + "/" + g;

                    Timber.e(uri.getPath());
                    Timber.e(g);
                    Timber.e(pp);

                    if (new File(g).exists()) {
                        files.add(new File(g));
                    } else if (new File(pp).exists()){
                        files.add(new File(pp));
                    }

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
