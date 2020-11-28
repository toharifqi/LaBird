package com.bima.toharifqi.labird.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.ZoomPicActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class PicSliderAdapter extends PagerAdapter {
    private List<String> picList;
    private LayoutInflater layoutInflater;
    private Context context;
    AlertDialog dialog;

    public PicSliderAdapter(Context context) {
        this.context = context;
    }

    public PicSliderAdapter(List<String> picList, Context context) {
        this.picList = picList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_pic, container, false);

        ImageView picImage;

        picImage = view.findViewById(R.id.picImage);

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Mohon tunggu...");

        final ProgressBar progressBar = view.findViewById(R.id.progressbar_pic);


        Glide.with(context).load(picList.get(position)).placeholder(R.drawable.species_placeholder).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(picImage);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ZoomPicActivity.class);
                intent.putExtra("imageUrl",picList.get(position));

                context.startActivity(intent);
            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
