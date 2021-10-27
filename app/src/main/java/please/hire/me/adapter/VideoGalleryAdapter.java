package please.hire.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import please.hire.me.Constant;
import please.hire.me.R;
import please.hire.me.UriHelper;
import please.hire.me.databinding.FileRvRowBinding;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {

    private final Context context;
    private final List<File> files;
    private final OnVideoGallery onVideoGallery;

    public interface OnVideoGallery {
        void onSelect(File file);
        void onCompress(File file);
    }

    public VideoGalleryAdapter(Context context, List<File> files, OnVideoGallery onVideoGallery) {
        this.context = context;
        this.files = files;
        this.onVideoGallery = onVideoGallery;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.file_rv_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        File file = files.get(position);

        Bitmap bm = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 70, stream);

        Glide.with(context)
                .asBitmap()
                .load(stream.toByteArray())
                .into(holder.view.iv);

        holder.view.title.setText(file.getName());

        long sizeMB = file.length() / (1024 * 1024);
        long size = Math.round(sizeMB * 100) / 100;

        holder.view.price.setText("Size : " + size + " MB");

        if (file.length() < Constant.TEN_MB_IN_BYTES) {
            holder.view.btnCompressLayout.setVisibility(View.GONE);
        }

        holder.view.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoGallery.onSelect(file);
            }
        });

        holder.view.btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoGallery.onCompress(file);
            }
        });
    }

    public void update(File file) {
        files.add(file);
        notifyItemInserted(files.size() - 1);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FileRvRowBinding view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = DataBindingUtil.bind(itemView);
        }
    }
}
